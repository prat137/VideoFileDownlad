package com.example.demoapp.ui.common

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState (
    val status: Status,
    val message: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}
