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

    private val hiraganaList = mutableListOf<JapaneseWord>()
    private val katakanaList = mutableListOf<JapaneseWord>()
    private val numberList = mutableListOf<JapaneseWord>()
    private val datesList = mutableListOf<JapaneseWord>()
    private val vocabularyList = mutableListOf<JapaneseWord>()
    private val allContent = mutableListOf<JapaneseWord>()

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

    fun load(content:String): List<Word>? {
        readFromFile(content)?.let {
            return Gson().fromJson(readFromFile(content), Array<Word>::class.java).toList()
            //return Gson().fromJson(readFromFile(), ContentResponse::class.java)
        }
        return null
    }

    fun loadAllContent(){
        getHiraganas()
        getKatakanas()
        getNumberList()
        getDatesList()
        getVocabularyList()
        /*allContent.clear()
        allContent.addAll(hiraganaList)
        allContent.addAll(numberList)
        allContent.addAll(datesList)*/
    }

    private fun getHiraganas() {
        hiraganaList.ifEmpty {
            val jsonFileString = getJsonDataFromAsset("hiragana")
            val gson = Gson()
            val listWordType = object : TypeToken<List<JapaneseWord>>() {}.type
            hiraganaList.addAll(gson.fromJson(jsonFileString, listWordType))
        }
    }

    private fun getKatakanas() {
        katakanaList.ifEmpty {
            val jsonFileString = getJsonDataFromAsset("katakana")
            val gson = Gson()
            val listWordType = object : TypeToken<List<JapaneseWord>>() {}.type
            katakanaList.addAll(gson.fromJson(jsonFileString, listWordType))
        }
    }

    private fun getNumberList() {
        numberList.ifEmpty {
            val jsonFileString = getJsonDataFromAsset("numbers")
            val gson = Gson()
            val listWordType = object : TypeToken<List<JapaneseWord>>() {}.type
            numberList.addAll(gson.fromJson(jsonFileString, listWordType))
        }
    }

    private fun getDatesList() {
        datesList.ifEmpty {
            val jsonFileString = getJsonDataFromAsset("dates")
            val gson = Gson()
            val listWordType = object : TypeToken<List<JapaneseWord>>() {}.type
            datesList.addAll(gson.fromJson(jsonFileString, listWordType))
        }
    }

    private fun getVocabularyList() {
        vocabularyList.ifEmpty {
            val jsonFileString = getJsonDataFromAsset("vocabulary")
            val gson = Gson()
            val listWordType = object : TypeToken<List<JapaneseWord>>() {}.type
            vocabularyList.addAll(gson.fromJson(jsonFileString, listWordType))
        }
    }

    private fun getAllContent(): List<JapaneseWord> {
        allContent.ifEmpty {
            allContent.addAll(hiraganaList)
            allContent.addAll(numberList)
            allContent.addAll(datesList)
            allContent.addAll(katakanaList)
        }
        return allContent
    }

    fun get(groupName: String): List<JapaneseWord> {
        return getAllContent().filter { it.type == groupName }
    }

    fun getAllContentGroups() : Map<String, List<JapaneseWord>>{
        return getAllContent().groupBy { it.type }
    }

    fun getGeneralGroups(): Map<String, List<JapaneseWord>> {
        val content = mutableMapOf<String, List<JapaneseWord>>()
        content.putAll(mapOf("Hiragana" to hiraganaList))
        content.putAll(mapOf("Katakana" to katakanaList))
        content.putAll(mapOf("Vocabulary" to vocabularyList))
        return content
    }

    fun getHiraganaGroups(): Map<String, List<JapaneseWord>> {
        return hiraganaList.groupBy { it.type }
    }

    fun getKatakanaGroups(): Map<String, List<JapaneseWord>> {
        return katakanaList.groupBy { it.type }
    }

    fun getNumberGroups(): Map<String, List<JapaneseWord>> {
        return numberList.groupBy { it.type }
    }

    fun getDatesGroups(): Map<String, List<JapaneseWord>> {
        return datesList.groupBy { it.type }
    }

    fun getVocabularyGroups(): Map<String, List<JapaneseWord>> {
        return vocabularyList.groupBy { it.type }
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