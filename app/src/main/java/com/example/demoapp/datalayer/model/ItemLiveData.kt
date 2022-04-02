package com.example.demoapp.datalayer.model

class ItemLiveData<T,K> {
    var code: Int = 0
    var data : T? = null
    var response :K? = null
    var other : Any? = null
}