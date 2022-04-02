package com.example.demoapp.dagger


import com.example.demoapp.base.BaseApplication
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun inject(application: BaseApplication)

}