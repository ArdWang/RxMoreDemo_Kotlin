package com.rx.rxmore.base.data.responsitory

import com.trello.rxlifecycle2.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class BaseRepository {
    //任何类型
    protected fun<T> observable(observable: Observable<T>, lifeProvider: LifecycleProvider<*>):Observable<T>{
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(lifeProvider.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread()) //指定在主线程中
    }
}