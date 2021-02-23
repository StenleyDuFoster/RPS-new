package com.stenleone.rockpaperscissors.ui.activitys

import android.os.Bundle
import android.widget.Toast
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.ActivityMainBinding
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity(override var layId: Int = R.layout.activity_main) : BaseActivity<ActivityMainBinding>() {

    override fun setup(savedInstanceState: Bundle?) {

    }

}