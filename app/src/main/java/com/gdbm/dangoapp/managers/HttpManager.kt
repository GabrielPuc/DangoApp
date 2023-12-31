package com.gdbm.dangoapp.managers

import android.util.Log
import com.gdbm.dangoapp.model.ApiResponse
import com.gdbm.dangoapp.model.ConfigResponse
import com.gdbm.dangoapp.model.ContentResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.URL


class HttpManager {

    fun asyncGetListRequest(
        endpoint: String,
        onSuccess: (ApiResponse<List<ContentResponse>>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(endpoint)
            val openedConnection =
                withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection
            openedConnection.requestMethod = GET

            val responseCode = openedConnection.responseCode
            try {
                val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
                val response = reader.readText()
                val apiResponse = ApiResponse(
                    responseCode,
                    getList(response,ContentResponse::class.java)
                )
                print(response)
                withContext(Dispatchers.IO) {
                    reader.close()
                }
                launch(Dispatchers.Main) {
                    onSuccess(apiResponse)
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                launch(Dispatchers.Main) {
                    onError(Exception("HTTP Request failed with response code $responseCode"))
                }
            } finally {

            }
        }
    }

    fun asyncGetConfigRequest(
        endpoint: String,
        onSuccess: (ApiResponse<ConfigResponse>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val url = URL(endpoint)
            val openedConnection =
                withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection
            openedConnection.requestMethod = GET

            val responseCode = openedConnection.responseCode
            try {
                val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
                val response = reader.readText()
                val apiResponse = ApiResponse(
                    responseCode,
                    parseJson<ConfigResponse>(response)
                )
                print(response)
                withContext(Dispatchers.IO) {
                    reader.close()
                }
                launch(Dispatchers.Main) {
                    onSuccess(apiResponse)
                }
            } catch (e: Exception) {
                Log.d("Error", e.message.toString())
                launch(Dispatchers.Main) {
                    onError(Exception("HTTP Request failed with response code $responseCode"))
                }
            } finally {

            }
        }
    }

    companion object {
        const val GET = "GET"
        const val POST = "POST"
    }

}


private inline fun <reified T>parseJson(text: String): T =
    Gson().fromJson(text, T::class.java)


private fun <T> getList(jsonArray: String?, clazz: Class<T>): List<T> {
    val typeOfT: Type = TypeToken.getParameterized(MutableList::class.java, clazz).type
    return Gson().fromJson(jsonArray, typeOfT)
}
