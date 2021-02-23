package com.stenleone.rockpaperscissors.ui.activitys

import android.os.Bundle
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.ActivityRegisterBinding
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity(override var layId: Int = R.layout.activity_main) : BaseActivity<ActivityRegisterBinding>() {

    override fun setup(savedInstanceState: Bundle?) {

    }

}