package com.example.demoapp.ui.login

import androidx.lifecycle.MutableLiveData
import com.example.demoapp.base.BaseViewModel
import com.example.demoapp.datalayer.model.ItemLiveData
import com.example.demoapp.datalayer.network.ApiInterface
import com.example.demoapp.datalayer.network.RetrofitProvider
import com.example.demoapp.utils.extension.PARAM_EMAIL
import com.example.demoapp.utils.extension.PARAM_PASSWORD
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class LoginViewModel : BaseViewModel() {



    private val responseLiveData =
        MutableLiveData<ItemLiveData<Any, Response<Any>>>()

    fun loginResponse(): MutableLiveData<ItemLiveData<Any, Response<Any>>> {
        return responseLiveData
    }

    internal fun requestForLogIn(
        username: String,
        password: String
    ): Disposable? {
        return requestForApi(userLogin(username,password), responseLiveData)
    }

    private fun userLogin(
        username: String,
        password: String
    ): Observable<Response<Any>> {
        val receiptJsonObject = JSONObject()
        receiptJsonObject.put(PARAM_EMAIL, username)
        receiptJsonObject.put(PARAM_PASSWORD, password)
        val body = RequestBody.create(
            MediaType.parse("application/json; charset=UTF-8"),
            receiptJsonObject.toString()
        )
        val apiService = RetrofitProvider.createService(ApiInterface::class.java)
        return apiService.userLogin(body) as Observable<Response<Any>>
    }
}