package com.gdbm.dangoapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdbm.dangoapp.config.Configs.ALL_CONTENT
import com.gdbm.dangoapp.config.Configs.CURRENT_LANGUAGE
import com.gdbm.dangoapp.config.Configs.ENDPOINT
import com.gdbm.dangoapp.managers.FileManager
import com.gdbm.dangoapp.managers.HttpManager
import com.google.gson.Gson

class MainViewModel : ViewModel() {
    val actionSelected = MutableLiveData<String>()

    fun loadAllContent(context: Context){
        val fileManager = FileManager(context, CURRENT_LANGUAGE)
        if(!fileManager.readFile(ALL_CONTENT).isNullOrEmpty()){
            return
        }
        val httpManager = HttpManager()
        httpManager.asyncGetListRequest(
            endpoint = "$ENDPOINT/$CURRENT_LANGUAGE/all",
            onSuccess = {
                val gson = Gson()
                it.response.iterator().forEach { item ->
                    fileManager.writeFile(item.name,gson.toJson(item.content))
                }
            },
            onError = {
                Log.e("ERROR",it.message.toString())
            })
    }
}