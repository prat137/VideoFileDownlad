package com.example.demoapp.datalayer.network

import com.example.demoapp.BuildConfig
import com.example.demoapp.utils.extension.AUTH_NAME
import com.example.demoapp.utils.extension.HEADER_NAME
import com.example.demoapp.utils.extension.errorLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null


    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        errorLog("response", message)
    }.setLevel(
        HttpLoggingInterceptor.Level.BODY
    )

    private val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(HEADER_NAME, AUTH_NAME)
            .addHeader("Accept-Encoding", "gzip, deflate")
            .build()
        chain.proceed(request)
    }


    private fun getHttpClient(): OkHttpClient? {

        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(interceptor)
                .build()
        }
        return okHttpClient

    }

    /**
     * Method to return retrofit instance. This method will retrofit retrofit instance with app api Base url.
     *
     * @return instance of ad retrofit.
     */

    private fun getRetrofit(): Retrofit {
        return if (retrofit != null) {
            retrofit as Retrofit
        } else {
            retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getHttpClient() as OkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            retrofit as Retrofit
        }
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return getRetrofit().create(serviceClass)
    }
}