package com.example.demoapp.datalayer.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class DownloadData() : Parcelable {


    var fileName: String? = ""

    var filePath: String? = ""

    constructor(parcel: Parcel) : this() {
        fileName = parcel.readString()
        filePath = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fileName)
        parcel.writeString(filePath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DownloadData> {
        override fun createFromParcel(parcel: Parcel): DownloadData {
            return DownloadData(parcel)
        }

        override fun newArray(size: Int): Array<DownloadData?> {
            return arrayOfNulls(size)
        }
    }

}