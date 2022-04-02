package com.example.demoapp.ui.splash.viewmodel


import com.example.demoapp.base.BaseViewModel
import com.example.demoapp.datalayer.network.ApiInterface
import javax.inject.Inject


class SplashViewModel : BaseViewModel() {
    @Inject
    lateinit var apiInterface: ApiInterface


    /*internal fun requestForIntroduction(
        context: Context
    ): Disposable? {
        return context.isNetworkAvailableObservable()
            .filter { true }
            .flatMap { introductionData() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())?.subscribe({ response ->
                responseLiveData.value = response
            }) { throwable ->
                responseLiveData.value = null
                throwable?.let { Timber.e(it) }
            }
    }

    fun insertDataInDb() {
        repository.insertDataInDb()
    }

    fun updateDataInDb(introductionResponse: IntroductionResponse) {
        repository.updateDataInDb(introductionResponse)
    }


    private fun introductionData(): Observable<Response<IntroductionResponse>> {
        val apiService = RetrofitProvider.createService(ApiInterface::class.java)
        return apiService.introductions()
    }*/
}