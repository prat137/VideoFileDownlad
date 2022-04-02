package com.example.demoapp.dagger


import com.example.demoapp.ui.splash.viewmodel.SplashViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(splashViewModel: SplashViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector
        //fun networkModule(networkModule: NetworkModule): Builder
    }

}