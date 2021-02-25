package com.stenleone.rockpaperscissors.ui.fragments.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentMainBinding
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks

class MainFragment(override var layId: Int = R.layout.fragment_main) : BaseFragment<FragmentMainBinding>() {

    companion object {
        const val TAG = "MainFragment"
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            playButton.throttleClicks(
                {

                }, lifecycleScope
            )
        }
    }
}