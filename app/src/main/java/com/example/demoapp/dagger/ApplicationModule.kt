package com.example.demoapp.dagger

import android.app.Application
import com.example.demoapp.base.BaseApplication

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val baseApp: BaseApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return baseApp
    }
}