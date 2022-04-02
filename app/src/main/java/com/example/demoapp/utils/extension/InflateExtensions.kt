package com.example.demoapp.utils.extension

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import com.example.demoapp.base.BaseApplication

inline val AppCompatActivity.app get() = applicationContext as BaseApplication
inline val AndroidViewModel.app get() = getApplication<BaseApplication>()

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

inline fun <reified T> ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): T {
    return inflate(layoutId, attachToRoot) as T
}