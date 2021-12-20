package com.khs.riotapiproject.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivityForViewBinding<T: ViewDataBinding>: AppCompatActivity() {
    abstract val layoutID: Int
    lateinit var viewDataBinding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding = DataBindingUtil.setContentView(this, layoutID)
        viewDataBinding.lifecycleOwner = this

        init()
    }

    abstract fun init()
}