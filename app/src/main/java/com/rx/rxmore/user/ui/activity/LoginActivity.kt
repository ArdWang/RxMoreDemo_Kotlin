package com.rx.rxmore.user.ui.activity

import android.os.Bundle
import com.rx.rxmore.MainActivity
import com.rx.rxmore.R
import com.rx.rxmore.base.ui.activity.BaseMvpActivity
import com.rx.rxmore.user.model.User
import com.rx.rxmore.user.presenter.UserPresenter
import com.rx.rxmore.user.presenter.view.UserView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


@Suppress("SENSELESS_COMPARISON")
class LoginActivity :BaseMvpActivity<UserPresenter>(),UserView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        mPresenter.mView = this
        mPresenter.lifeProvider = this
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_login
    }

    override fun initView(){
        mLogin.setOnClickListener{
            getUser()
        }
    }

    private fun getUser() {
        mPresenter.getUser(mPhoneEt!!.text.toString(), mPwdEt!!.text.toString(), "111")
    }


    override fun onGetUserResult(user: User) {
        if(user!=null) {
            startActivity<MainActivity>("User" to user)
        }
    }
}