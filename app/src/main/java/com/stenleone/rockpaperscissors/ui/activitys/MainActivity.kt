package com.stenleone.rockpaperscissors.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.ActivityMainBinding
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import com.stenleone.rockpaperscissors.ui.fragments.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity(override var layId: Int = R.layout.activity_main) : BaseActivity<ActivityMainBinding>() {

    override fun setup(savedInstanceState: Bundle?) {

        checkAuth()
        setupMainFragment()
    }

    private fun checkAuth() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setupMainFragment() {
        fragmentContainerId = binding.rootView.id
        if (supportFragmentManager.findFragmentByTag(MainFragment.TAG) == null) {
            addFragment(null, MainFragment(), MainFragment.TAG)
        }
    }

}