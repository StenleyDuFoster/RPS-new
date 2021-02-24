package com.stenleone.rockpaperscissors.ui.activitys.base

import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: T
    protected abstract var layId: Int
    @IntegerRes
    protected open var fragmentContainerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layId)
        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)

    fun addFragment(fragmentToHide: Fragment?, fragment: Fragment, tag: String? = null) {
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
//            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
            .add(fragmentContainerId, fragment, tag ?: fragment.javaClass.name)
            .addToBackStack(null)

        fragmentToHide?.let { it ->
            fragmentTransaction.hide(it)
        }

        fragmentTransaction.commit()
    }
}