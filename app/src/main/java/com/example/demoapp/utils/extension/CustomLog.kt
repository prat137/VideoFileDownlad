package com.example.demoapp.utils.extension

import android.util.Log
import com.example.demoapp.BuildConfig

/**
     * Method to print verbose information
     * @param tag key
     * @param message value to be printed
     */
    fun verbose(tag: String, message: Any) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message.toString())
    }

    /**
     * Method to print warning information
     * @param tag key
     * @param message value to be printed
     */
    fun warning(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.w(tag, message)
    }

    /**
     * Method to print debug information
     * @param tag key
     * @param message value to be printed
     */
    fun debug(tag: String, message: Any) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message.toString())
    }

    /**
     * Method to print infoLog information
     * @param tag key
     * @param message value to be printed
     */

    fun infoLog(tag: String, message: String) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message)
    }

    /**
     * Method to print errorLog information
     * @param tag key
     * @param message value to be printed
     */
    fun errorLog(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)

        }
    }


    /**
     * Method to print errorLog  information with throwable
     * @param tag key
     * @param message value to be printed
     * @param throwable exception throwable
     */
    fun errorLog(tag: String, message: String, throwable: Throwable) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message, throwable)
    }
