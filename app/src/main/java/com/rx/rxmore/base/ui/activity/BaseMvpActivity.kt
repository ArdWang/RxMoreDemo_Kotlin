package com.rx.rxmore.base.ui.activity

import android.os.Bundle
import android.view.View
import com.rx.rxmore.base.presenter.BasePresenter
import com.rx.rxmore.base.presenter.view.BaseView
import com.rx.rxmore.utils.PMUtils
import com.rx.rxmore.widgets.ProgressLoading
import org.jetbrains.anko.toast

abstract class BaseMvpActivity<T:BasePresenter<*>>:BaseActivity(),BaseView,View.OnClickListener {

    lateinit var mPresenter: T

    lateinit var progressLoading:ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setContentView(getLayoutResID())
        initView()
        mPresenter = PMUtils.getT(this,0)!!
        //mPresenter.mView = this

        progressLoading = ProgressLoading.create(this)

    }


    abstract fun getLayoutResID():Int

    protected open fun initView() {

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

    override fun onClick(v: View?) {

    }

}


