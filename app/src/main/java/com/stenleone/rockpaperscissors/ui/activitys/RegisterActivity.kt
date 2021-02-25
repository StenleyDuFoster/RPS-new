package com.stenleone.rockpaperscissors.ui.activitys

import android.os.Bundle
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.ActivityRegisterBinding
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister.SignInOrRegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity(override var layId: Int = R.layout.activity_register) : BaseActivity<ActivityRegisterBinding>() {

    override fun setup(savedInstanceState: Bundle?) {

        setupFragment()
    }

    private fun setupFragment() {
        binding.apply {

            fragmentContainerId = rootView.id

            if (supportFragmentManager.findFragmentByTag(SignInOrRegisterFragment.TAG) == null) {
                addFragment(null, SignInOrRegisterFragment(), SignInOrRegisterFragment.TAG)
            }
        }
    }

}