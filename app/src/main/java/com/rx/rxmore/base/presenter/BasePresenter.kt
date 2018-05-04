package com.rx.rxmore.base.presenter

import com.rx.rxmore.base.common.MainApplication
import com.rx.rxmore.base.presenter.view.BaseView
import com.rx.rxmore.utils.NetWorkUtils
import com.trello.rxlifecycle2.LifecycleProvider

open class BasePresenter<T:BaseView> {

    lateinit var mView:T

    lateinit var lifeProvider: LifecycleProvider<*>

    fun checkNetWork():Boolean{
        if (NetWorkUtils.isNetWorkAvailable(MainApplication.context)) {
            return true
        }
        mView.onError("网络不可用")
        return false
    }

}