package com.example.demoapp.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.example.demoapp.dagger.ApplicationComponent
import com.example.demoapp.dagger.ApplicationModule
import com.example.demoapp.dagger.DaggerApplicationComponent
import com.example.demoapp.datalayer.storage.AppPref
import com.example.demoapp.utils.extension.setWindowDimensions
import java.util.*

class BaseApplication : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setUp()
        AppPref.init(this)
//        FirebaseApp.initializeApp(this)
        setWindowDimensions()

    }

    private fun setUp() {
        component =
            DaggerApplicationComponent.builder().applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    companion object {
        var isClockScreenToBg = true
        lateinit var instance: BaseApplication private set
    }

    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }

    /**
     * This method will create unique id by using date time and if any exception is occur, are using random class to
     * create unique id for notification.
     */
    fun getUniqueNotificationId(): Int {
        val notificationId: Int
        notificationId = try {
            (Date().time / 1000L % Int.MAX_VALUE).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            val random = Random()
            random.nextInt(9999 - 1000) + 10
        }
        return notificationId
    }
}