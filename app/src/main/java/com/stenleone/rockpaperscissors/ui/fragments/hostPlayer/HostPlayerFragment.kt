package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import android.os.Bundle
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment

class HostPlayerFragment(override var layId: Int = R.layout.fragment_player) : BaseFragment<FragmentPlayerBinding>() {

    companion object {
        const val TAG = "HostPlayerFragment"
    }

    override fun setup(savedInstanceState: Bundle?) {

    }

}