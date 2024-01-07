package com.gdbm.dangoapp.model

data class ApiResponse<T>(
    val responseCode: Int,
    val response: T
)

data class NewResponse<T>(
    val success: Boolean,
    val response: T
)

data class ConfigResponse(
    val content: List<ConfigModel>
)

data class ContentResponse(
    val content: List<Word>,
    val name:String,
    val version:Double
)