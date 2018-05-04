package com.rx.rxmore

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.rx.rxmore.user.model.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        mUser = intent.getParcelableExtra("User")
        mUserTv.text = mUser.phone + "/" + mUser.username + "/" + mUser.password
    }
}
