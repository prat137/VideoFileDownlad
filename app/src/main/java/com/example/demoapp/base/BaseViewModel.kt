package com.example.demoapp.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demoapp.R
import com.example.demoapp.dagger.DaggerViewModelInjector

import com.example.demoapp.dagger.ViewModelInjector
import com.example.demoapp.datalayer.model.BaseResponse
import com.example.demoapp.datalayer.model.ItemLiveData
import com.example.demoapp.datalayer.network.ApiInterface
import com.example.demoapp.datalayer.network.RetrofitProvider
import com.example.demoapp.utils.extension.*

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.*

abstract class BaseViewModel : ViewModel() {
    /*companion object {
        val isJobAccept = MutableLiveData<Boolean>()
        val isJobComplete = MutableLiveData<Boolean>()
        val isJobCancelled = MutableLiveData<Boolean>()
        val isJobAvailable = MutableLiveData<Boolean>()
        val updateResponse = MutableLiveData<UpdateProfileResponse>()
    }*/

    private val injector: ViewModelInjector = DaggerViewModelInjector.builder()
        //.networkModule(NetworkModule)
        .build()

/*    init {
        inject()
    }*/

    //private val otherSettingApiLiveData = MutableLiveData<Response<OtherAppSettingResponse>>()

/*    *//**
     * Injects the required dependencies
     *//*
    private fun inject() {
        when (this) {
            is SplashViewModel -> injector.inject(this)
            is LoginViewModel -> injector.inject(this)
        }
    }*/

    fun requestForApi(
        userSignUp: Observable<Response<Any>>,
        responseLiveData: MutableLiveData<ItemLiveData<Any, Response<Any>>>
    ): Disposable? {
        return BaseApplication.instance.applicationContext.isNetworkAvailableObservable()
            .filter { true }
            .flatMap { userSignUp }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())?.subscribe({ response ->
                updateLiveData(response, null, responseLiveData)
            }) { throwable ->
                if (throwable is ConnectException || throwable is UnknownHostException)
                    updateLiveData(null, throwable, responseLiveData)
                else {
                    updateLiveData(null, null, responseLiveData)
                    //throwable.message?.let { Timber.e(it) }
                }
            }
    }

    private fun updateLiveData(
        response: Response<Any>?,
        other: Any?,
        responseLiveData: MutableLiveData<ItemLiveData<Any, Response<Any>>>
    ) {
        val itemData = ItemLiveData<Any, Response<Any>>()
        itemData.data = null
        itemData.response = response
        itemData.other = other
        itemData.code = response?.code() ?: kotlin.run { 0 }
        responseLiveData.value = itemData
    }


    fun requestForLogout(token: String): Disposable? {
        return BaseApplication.instance.applicationContext.isNetworkAvailableObservable()
            .filter { true }
            .flatMap { logoutData(token) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                if (response.code() == 500) {
                    BaseApplication.instance.applicationContext.showToast(
                        BaseApplication.instance.applicationContext.getString(R.string.internal_server_error),
                        showInReleaseBuild = false
                    )
                }
            }) { throwable ->
                throwable.message?.let { errorLog("error", it) }
            }
    }

    private fun logoutData(token: String): Observable<Response<BaseResponse>> {
        val headerParams = HashMap<String, Any?>()
        headerParams[PARAM_TOKEN] = token
        val param= JSONObject()
        param.put("deviceType", PARAM_DEVICE)
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=UTF-8"),
            param.toString()
        )
        val apiService = RetrofitProvider.createService(ApiInterface::class.java)
        return apiService.logout(headerParams,body)
    }


}