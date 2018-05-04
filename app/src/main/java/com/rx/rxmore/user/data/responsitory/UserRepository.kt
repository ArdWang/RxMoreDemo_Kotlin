package com.rx.rxmore.user.data.responsitory

import com.rx.rxmore.base.data.net.RetrofitFactory
import com.rx.rxmore.base.data.responsitory.BaseRepository
import com.rx.rxmore.base.rx.BaseFunction
import com.rx.rxmore.user.data.api.UserApi
import com.rx.rxmore.user.data.protocol.GetUserReq
import com.rx.rxmore.user.model.User
import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable

class UserRepository:BaseRepository(){
    fun getUser(phone:String, password:String, pushId:String, lifeProvider:LifecycleProvider<*>): Observable<User> {
        return observable(RetrofitFactory.instance.create(UserApi::class.java).getUser(GetUserReq(phone,password,pushId))
                ,lifeProvider).map(BaseFunction())
    }
}














