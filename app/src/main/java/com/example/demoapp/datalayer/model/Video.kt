package com.example.demoapp.datalayer.model

data class Video(
    val description: String,
    val sources: List<String>,
    val subtitle: String,
    val thumb: String,
    val title: String,
    var isDownloaded: Boolean,
    var isDownloadStarted: Boolean,
    var path: String
)