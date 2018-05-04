package com.rx.rxmore.base.data.protocol

data class BaseResp<out T>(val code:String,val message:String,val data:T)
