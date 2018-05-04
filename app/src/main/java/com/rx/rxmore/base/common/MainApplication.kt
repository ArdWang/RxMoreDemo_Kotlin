package com.rx.rxmore.base.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MainApplication :Application(){
    override fun onCreate() {
        super.onCreate()
        context = this
    }

    /*
       全局伴生对象
    */
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}