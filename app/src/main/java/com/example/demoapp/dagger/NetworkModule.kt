package com.example.demoapp.dagger

import android.content.Context
import com.example.demoapp.BuildConfig.BASE_URL

import com.example.demoapp.datalayer.network.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.io.File

@Module
@Suppress
object NetworkModule {

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCaseApi(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }


    @Provides
    @Reusable
    @JvmStatic
    internal fun provideHttpClient(logger: HttpLoggingInterceptor, cache: Cache): OkHttpClient {

        val builder = OkHttpClient().newBuilder()
        builder.addInterceptor(logger)
        builder.cache(cache)
        return builder.build()
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCache(file: File): Cache {
        return Cache(file, (10 * 10 * 1000).toLong())
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideCacheFile(context: Context): File {
        return context.filesDir
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideGsonClient(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }
}