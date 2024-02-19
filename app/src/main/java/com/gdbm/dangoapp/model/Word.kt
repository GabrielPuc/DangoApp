package com.gdbm.dangoapp.model

data class Word(
    val symbol:String,
    val meaning:String,
    val group:String,
    val pronunciation:String?,
    val latinBased:String?,
    var type:String?
)