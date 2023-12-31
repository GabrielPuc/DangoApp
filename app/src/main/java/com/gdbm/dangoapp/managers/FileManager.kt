package com.gdbm.dangoapp.managers

import android.content.Context
import java.io.File
import java.nio.charset.StandardCharsets

class FileManager(private val applicationContext: Context, folder: String) {

    private val altDirectory = File("${applicationContext.filesDir.path}/$folder")


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
        if (!altDirectory.exists()) {
            altDirectory.mkdir()
        }
        return File(altDirectory.path, fileName)
    }

}