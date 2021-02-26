package com.stenleone.rockpaperscissors.ui.fragments.profile

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentProfileBinding
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(override var layId: Int = R.layout.fragment_profile) : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.profile.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.email, Toast.LENGTH_LONG).show()
        }
    }
}