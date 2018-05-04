package com.rx.rxmore.user.data.api


import com.rx.rxmore.base.data.protocol.BaseResp
import com.rx.rxmore.user.data.protocol.GetUserReq
import com.rx.rxmore.user.model.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user/getUser")
    fun getUser(@Body req: GetUserReq):Observable<BaseResp<User>>
}