package com.example.cfinanceapp.data.models

import com.squareup.moshi.Json

data class Status(

val timestamp: String,

@Json(name = "error_code")
val errorCode: Int? =null,

@Json(name = "error_message")
val errorMessage: String? = null,

val elapsed: Int,

@Json(name = "credit_count")
val creditCount: Int,

val notice: String? = null

)

