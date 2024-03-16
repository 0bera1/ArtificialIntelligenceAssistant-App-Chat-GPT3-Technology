package com.example.myapplication.models.request

data class ImageGenerateRequest(
   // val model: String,
    val n: Int,
    val prompt: String,
    val size: String
)