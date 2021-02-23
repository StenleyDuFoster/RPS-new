package com.stenleone.rockpaperscissors.ui.activitys.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: T
    protected abstract var layId: Int

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        binding = DataBindingUtil.setContentView(this, layId)
        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)
}