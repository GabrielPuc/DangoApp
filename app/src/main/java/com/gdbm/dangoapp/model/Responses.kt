package com.gdbm.dangoapp.model

data class ApiResponse<T>(
    val responseCode: Int,
    val response: T
)

data class ListResponse(
    val content: List<Word>
)

data class ConfigResponse(
    val content: ConfigModel
)

data class ContentResponse(
    val content: List<Word>,
    val name:String
)