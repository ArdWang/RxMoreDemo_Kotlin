package com.rx.rxmore.base.rx

import com.rx.rxmore.base.data.protocol.BaseResp
import io.reactivex.functions.Function

/**
    通用数据类型转换封装
 */
class BaseFunction<T>: Function<BaseResp<T>, T> {
    override fun apply(t: BaseResp<T>): T {
        if (t.code != "200"){
            BaseException(t.code,t.message)
        }
        return t?.data
    }
}