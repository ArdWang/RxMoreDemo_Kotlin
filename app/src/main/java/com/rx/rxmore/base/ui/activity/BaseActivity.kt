package com.rx.rxmore.base.ui.activity

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

open class BaseActivity:RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}