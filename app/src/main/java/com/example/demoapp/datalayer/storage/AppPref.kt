package com.example.demoapp.datalayer.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


object AppPref {
    private const val PREF_NAME = "user_data"
    private lateinit var sharedPreferences: SharedPreferences

    private const val PREF_NAME_APP = "app_data"
    private lateinit var appSharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
        appSharedPreferences = context.getSharedPreferences(PREF_NAME_APP, Activity.MODE_PRIVATE)
    }

    fun getValue(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getValue(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    fun getStringSet(key: String, stringSet: Set<String>): Set<String>? {
        return sharedPreferences.getStringSet(key, stringSet)
    }

    //############Set values################

    fun setValue(key: String, defaultValue: String?) {
        sharedPreferences.edit().putString(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Int) {
        sharedPreferences.edit().putInt(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Long) {
        sharedPreferences.edit().putLong(key, defaultValue).apply()

    }

    fun setValue(key: String, defaultValue: Boolean) {
        sharedPreferences.edit().putBoolean(key, defaultValue).apply()
    }

    fun setValue(key: String, defaultValue: Float) {
        sharedPreferences.edit().putFloat(key, defaultValue).apply()
    }

    fun setStringSet(key: String, stringSet: Set<String>) {
        sharedPreferences.edit().putStringSet(key, stringSet).apply()
    }

    /* private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
         val editor = this.edit()
         operation(editor)
         editor.apply()
     }

     inline fun SharedPreferences.clear(operation: (SharedPreferences.Editor) -> Unit) {
         val editor = this.edit()
         operation(editor)
         editor.clear().apply()
     }

     fun SharedPreferences.setValue(key: String, value: Any?) {
         when (value) {
             is String? -> edit { it.putString(key, value) }
             is Int -> edit { it.putInt(key, value) }
             is Boolean -> edit { it.putBoolean(key, value) }
             is Float -> edit { it.putFloat(key, value) }
             is Long -> edit { it.putLong(key, value) }
             else -> throw UnsupportedOperationException("Not yet implemented")
         }
     }

     inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
         return when (T::class) {
             String::class -> getString(key, defaultValue as? String) as T?
             Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
             Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
             Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
             Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
             else -> throw UnsupportedOperationException("Not yet implemented")
         }
     }
 */

    fun getAppValue(key: String, defaultValue: Boolean): Boolean {
        return appSharedPreferences.getBoolean(key, defaultValue)
    }


    fun setAppValue(key: String, defaultValue: Boolean) {
        appSharedPreferences.edit().putBoolean(key, defaultValue).apply()
    }

    fun getAppValue(key: String, defaultValue: String): String? {
        return appSharedPreferences.getString(key, defaultValue)
    }


    fun setAppValue(key: String, defaultValue: String) {
        appSharedPreferences.edit().putString(key, defaultValue).apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}