package com.gdbm.dangoapp.managers

import android.content.Context
import android.content.res.AssetManager
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.utils.extensions.md5
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets


class FileManager(private val applicationContext: Context, folder: String? = "") {

    private val filesCachedDirectory = File("${applicationContext.filesDir.path}/$folder")
    private val tessdataDirectory = File("${applicationContext.filesDir.path}/tessdata")

    fun readFile(fileToReadName: String) : String? {
        try {
            val fileToRead = makeFile(fileToReadName)
            if (!fileToRead.exists()) {
                return null
            }

            return fileToRead.readText(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            return null
        }
    }

    fun writeFile(fileToWriteName: String, plainContent: String?) {
        try {
            val fileToWrite = makeFile(fileToWriteName)
            val fileContent = plainContent?.toByteArray(StandardCharsets.UTF_8)
                ?: "".toByteArray(StandardCharsets.UTF_8)
            fileToWrite.writeBytes(fileContent)
        } catch(e: Exception) {
            return
        }
    }

    private fun makeFile(fileName: String): File {
        if (!filesCachedDirectory.exists()) {
            filesCachedDirectory.mkdir()
        }
        return File(filesCachedDirectory.path, fileName)
    }

    fun verifyTrainedModelsIntegrity() {
        val trainedModel = makeFile("tessdata/jpn.traineddata")
        if (trainedModel.exists() && (trainedModel.md5() == Configs.JAPANESE_HASH)) {
            return
        }
        extractAssets()
    }
    private fun extractAssets() {
        val assetsManager = applicationContext.assets
        val filesDir: File = applicationContext.filesDir
        val tessDir = File(filesDir, "tessdata")
        if (!tessDir.exists() && !tessDir.mkdir()) {
            throw RuntimeException("Can't create directory $tessDir")
        }

        try {
            for (assetName in assetsManager.list("tesseract")!!) {
                val targetFile: File = if (assetName.endsWith(".traineddata")) {
                    File(tessDir, assetName)
                } else {
                    File(filesDir, assetName)
                }
                if (!targetFile.exists()) {
                    copyFile(assetsManager, assetName, targetFile)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun copyFile(
        assetsManager: AssetManager,
        assetName: String,
        outFile: File
    ) {
        try {
            assetsManager.open("tesseract/$assetName").use { `in` ->
                FileOutputStream(outFile).use { out ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (`in`.read(buffer).also { read = it } != -1) {
                        out.write(buffer, 0, read)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}