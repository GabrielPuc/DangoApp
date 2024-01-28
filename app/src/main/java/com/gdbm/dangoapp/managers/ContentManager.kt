package com.gdbm.dangoapp.managers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.gdbm.dangoapp.model.ContentResponse
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.model.Word
import com.gdbm.dangoapp.utils.Preferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ContentManager private constructor(val context: Context) {

    fun retrieve(content:String): List<Word>? {
        readFromFile(content)?.let {
            return Gson().fromJson(readFromFile(content), Array<Word>::class.java).toList()
        }
        return null
    }

    @SuppressLint("SimpleDateFormat")
    fun needToUpdateFiles(): Boolean {
        val formatter = SimpleDateFormat("dd/MM/yy")
        val lastUpdateDate = Preferences.getLastUpdate(context)?.let { formatter.parse(it) }
        val calendar: Calendar = Calendar.getInstance()
        return if(lastUpdateDate != null){
            calendar.time = lastUpdateDate
            calendar.add(Calendar.DATE,7)
            Date().after(calendar.time)
        } else {
            true
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun fetchAllContent() {
        val fileManager = FileManager(context, Configs.CURRENT_LANGUAGE)
        val httpManager = HttpManager()

        runBlocking {
            val request = httpManager.getContentListRequest("${Configs.ENDPOINT}/${Configs.CURRENT_LANGUAGE}/all")
            if(request.success){
                val map = mutableMapOf<String,Double>()
                val gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).create()
                request.response.iterator().forEach { item ->
                    fileManager.writeFile(item.name,gson.toJson(item.content))
                    map[item.name] = item.version
                }
                fileManager.writeFile(Configs.CONTENT_FOR_ + Configs.CURRENT_LANGUAGE,gson.toJson(map))
                val currentDate = SimpleDateFormat("dd/MM/yyy").format(Date())
                Preferences.setLastUpdate(currentDate, context)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateContent(currentVersion: Map<String,Double>) {
        val listToUpdate = mutableListOf<String>()
        val retrievedVersionsList =  fetchContentVersions()
        val retrievedVersions = retrievedVersionsList.associate { it.name to it.version }
        retrievedVersions.let {
            retrievedVersions.iterator().forEach { element ->
                if(currentVersion.containsKey(element.key)){
                    if(currentVersion[element.key]!! < element.value){
                        listToUpdate.add(element.key)
                    }
                }else{
                    listToUpdate.add(element.key)
                }
            }
        }

        val httpManager = HttpManager()
        val fileManager = FileManager(context, Configs.CURRENT_LANGUAGE)
        val gson = Gson()
        listToUpdate.iterator().forEach {
            httpManager.asyncGetContentRequest(
                endpoint = "${Configs.ENDPOINT}/${Configs.CURRENT_LANGUAGE}/$it",
                onSuccess = { objResponse ->
                    fileManager.writeFile(objResponse.response.name,gson.toJson(objResponse.response.content))
                },
                onError = { objError ->
                    Log.e("GET CONTENT REQUEST",objError.message.toString())
                }
            )
        }
        fileManager.writeFile(Configs.CONTENT_FOR_ + Configs.CURRENT_LANGUAGE,gson.toJson(retrievedVersions))
        val currentDate = SimpleDateFormat("dd/MM/yyy").format(Date())
        Preferences.setLastUpdate(currentDate, context)
    }

    fun contentAvailable(): Map<String, Double>? {
        return loadContentVersions()
    }

    private fun readFromFile(name:String): String? {
        val fileManager = FileManager(context, Configs.CURRENT_LANGUAGE)
        return fileManager.readFile(name)
    }

    private fun loadContentVersions(): Map<String, Double>? {
        val fileManager = FileManager(context, Configs.CURRENT_LANGUAGE)
        val versionsString = fileManager.readFile(Configs.CONTENT_FOR_ + Configs.CURRENT_LANGUAGE) ?: return null
        val gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.DOUBLE).create()
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(versionsString, type)
    }

    private fun fetchContentVersions(): List<ContentResponse> {
        val httpManager = HttpManager()
        val contentResponse = mutableListOf<ContentResponse>()

        runBlocking {
            val request = httpManager.getContentListRequest("${Configs.ENDPOINT}/versions/${Configs.CURRENT_LANGUAGE}")
            if(request.success){
                request.response.iterator().forEach { dm ->
                    contentResponse.add(dm)
                }
            }
        }

        return contentResponse
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