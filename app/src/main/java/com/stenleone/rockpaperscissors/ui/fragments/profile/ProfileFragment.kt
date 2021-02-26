package com.stenleone.rockpaperscissors.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentProfileBinding
import com.stenleone.rockpaperscissors.ui.dialogs.InfoDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(override var layId: Int = R.layout.fragment_profile) : BaseFragment<FragmentProfileBinding>() {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private val viewModel: ProfileViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModel()
        setupClicks()
    }

    private fun setupViewModel() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.profile.observe(viewLifecycleOwner) {
            binding.user = it
        }
        viewModel.updateSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                InfoDialogFragment.show(childFragmentManager, null , getString(R.string.profile_update_success))
            }
        }
        viewModel.inProgress.observe(viewLifecycleOwner) {
            if (it) {
                LoadingDialogFragment.show(childFragmentManager)
            } else {
                LoadingDialogFragment.cancel(childFragmentManager)
            }
        }
    }

    private fun setupClicks() {
        binding.apply {
            saveButton.throttleClicks(
                {
                    viewModel?.updateName()
                }, lifecycleScope
            )
            backButton.throttleClicks(
                {
                    requireActivity().onBackPressed()
                }, lifecycleScope
            )
        }
    }
}