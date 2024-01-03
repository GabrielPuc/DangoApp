package com.gdbm.dangoapp.managers

import android.annotation.SuppressLint
import android.content.Context
import com.gdbm.dangoapp.config.Configs
import com.gdbm.dangoapp.model.JapaneseWord
import com.gdbm.dangoapp.model.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ContentManager private constructor(val context: Context) {

    private fun getJsonDataFromAsset(fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open("jsons/${fileName}.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun readFromFile(name:String): String? {
        val fileManager = FileManager(context,Configs.CURRENT_LANGUAGE)
        return fileManager.readFile(name)
    }

    fun retrieve(content:String): List<Word>? {
        readFromFile(content)?.let {
            return Gson().fromJson(readFromFile(content), Array<Word>::class.java).toList()
            //return Gson().fromJson(readFromFile(), ContentResponse::class.java)
        }
        return null
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ContentManager? = null

        fun getInstance(context: Context): ContentManager {
            if (instance == null) {
                instance = ContentManager(context.applicationContext)
            }
            return instance!!
        }
    }
}