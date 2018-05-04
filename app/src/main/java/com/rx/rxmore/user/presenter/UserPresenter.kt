package com.rx.rxmore.user.presenter

import com.rx.rxmore.base.presenter.BasePresenter
import com.rx.rxmore.base.rx.BaseObserver
import com.rx.rxmore.user.data.responsitory.UserRepository
import com.rx.rxmore.user.model.User
import com.rx.rxmore.user.presenter.view.UserView


class UserPresenter:BasePresenter<UserView>() {

    private lateinit var userRepository: UserRepository

    fun getUser(phone: String, password: String, pushid: String){
        userRepository = UserRepository()

        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        userRepository.getUser(phone,password,pushid,lifeProvider).subscribe(object :BaseObserver<User>(mView){
            override fun onNext(t: User) {
                mView.onGetUserResult(t)
            }
        })


    }
}