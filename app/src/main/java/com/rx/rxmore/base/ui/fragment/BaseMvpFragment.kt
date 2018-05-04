package com.rx.rxmore.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rx.rxmore.base.common.MainApplication
import com.rx.rxmore.base.presenter.BasePresenter
import com.rx.rxmore.base.presenter.view.BaseView
import com.rx.rxmore.utils.PMUtils
import com.rx.rxmore.widgets.ProgressLoading
import org.jetbrains.anko.support.v4.toast

abstract class BaseMvpFragment<T:BasePresenter<*>> :BaseFragment(),BaseView,View.OnClickListener{

    lateinit var mPresenter: T

    lateinit var progressLoading: ProgressLoading


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        mPresenter = PMUtils.getT(this,0)!!
        progressLoading = ProgressLoading.create(MainApplication.context)
    }

    override fun onClick(p0: View?) {

    }

    override fun showLoading() {
        progressLoading.showLoading()
    }

    override fun hideLoading() {
        progressLoading.hideLoading()
    }

    override fun onError(text: String) {
        toast(text)
    }
}