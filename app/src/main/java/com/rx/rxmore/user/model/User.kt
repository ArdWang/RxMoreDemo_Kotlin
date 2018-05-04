package com.rx.rxmore.user.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
        val userid:Int,
        val username:String,
        val password:String,
        val age:Int,
        val sex:Int,
        val phone:String,
        val email:String,
        val note:String,
        val admin:Int,
        val sign:String
):Parcelable