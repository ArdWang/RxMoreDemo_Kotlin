package com.rx.rxmore.user.presenter.view


import com.rx.rxmore.base.presenter.view.BaseView
import com.rx.rxmore.user.model.User

interface UserView : BaseView {
    //获取成功
    fun onGetUserResult(user: User)
}
