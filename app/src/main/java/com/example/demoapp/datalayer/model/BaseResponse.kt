package com.example.demoapp.datalayer.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

open class BaseResponse() : Parcelable {

    @SerializedName("isError")
    var isError: Boolean = false
    @SerializedName("message")
    var errors: String? = null

    constructor(parcel: Parcel) : this() {
        isError = parcel.readByte() != 0.toByte()
        errors = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeString(errors)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseResponse> {
        override fun createFromParcel(parcel: Parcel): BaseResponse {
            return BaseResponse(parcel)
        }

        override fun newArray(size: Int): Array<BaseResponse?> {
            return arrayOfNulls(size)
        }
    }

}