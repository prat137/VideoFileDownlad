package com.example.demoapp.datalayer.network

import com.example.demoapp.datalayer.model.BaseResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST


interface ApiInterface {
    /**
     * User mail login api
     */
    @POST("api/login")
    fun userLogin(@Body bodyParams: RequestBody): Observable<Response<BaseResponse>>

    /**
     * User logout api
     */
    @POST("logout")
    fun logout(@HeaderMap headerMap: HashMap<String, Any?>, @Body bodyParams: RequestBody): Observable<Response<BaseResponse>>


}