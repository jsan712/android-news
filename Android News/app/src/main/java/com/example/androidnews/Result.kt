package com.example.androidnews

data class Result(
    val headline: String,
    val preview: String,
    val sourceName: String,
    val pictureURL: String,
    val url: String,
    val maxPages: Int
)