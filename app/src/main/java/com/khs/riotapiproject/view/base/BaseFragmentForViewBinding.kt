package com.khs.riotapiproject.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragmentForViewBinding<T: ViewDataBinding>: Fragment() {
    abstract val layoutID: Int
    lateinit var viewDataBinding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutID, container, false)
        viewDataBinding.lifecycleOwner = viewLifecycleOwner

        init()

        return viewDataBinding.root
    }

    abstract fun init()
}