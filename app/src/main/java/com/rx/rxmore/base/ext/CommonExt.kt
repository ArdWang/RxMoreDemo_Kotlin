package com.rx.rxmore.base.ext

import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//任何类型
fun<T> Observable<T>.excute(observable: Observable<T>, lifeProvider: LifecycleProvider<*>){
    this.subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .compose(lifeProvider.bindToLifecycle())
            .observeOn(AndroidSchedulers.mainThread()) //指定在主线程中
}

/**
    扩展数据转换
 */
//fun <T> BaseResp<T>.convert(): T {
    //return this.map(BaseFunction())
//}






