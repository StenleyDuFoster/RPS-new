package com.stenleone.rockpaperscissors.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentProfileBinding
import com.stenleone.rockpaperscissors.ui.dialogs.info.InfoDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogCallBack
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogFragment.Companion.ERROR_DIALOG_CONNECTION_ACTION
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogFragment.Companion.ERROR_DIALOG_UNKNOWN_ACTION
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister.SignInOrRegisterFragment
import com.stenleone.stanleysfilm.model.entity.RequestError
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(override var layId: Int = R.layout.fragment_profile) : BaseFragment<FragmentProfileBinding>(), ErrorDialogCallBack {

    companion object {
        const val TAG = "ProfileFragment"
    }

    private val viewModel: ProfileViewModel by viewModels()
    private var retryDialogRetryAction: (() -> Unit)? = null

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
        viewModel.error.observe(viewLifecycleOwner) {
            when (it.type) {
                RequestError.CONNECTION_ERROR -> {
                    ErrorDialogFragment.showStandart(
                        childFragmentManager,
                        getString(R.string.connection_text),
                        null,
                        getString(R.string.ok),
                        getString(R.string.retry),
                        ERROR_DIALOG_CONNECTION_ACTION
                    )
                    retryDialogRetryAction = it.retryAction
                }
                RequestError.REQUEST_ERROR -> {
                    ErrorDialogFragment.showStandart(
                        childFragmentManager,
                        getString(R.string.unknown_error),
                        it.message,
                        getString(R.string.ok),
                        getString(R.string.retry),
                        ERROR_DIALOG_UNKNOWN_ACTION
                    )
                    retryDialogRetryAction = it.retryAction
                }
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

    override fun errorDialogOkClick(type: Int) {
        ErrorDialogFragment.cancel(childFragmentManager)
    }

    override fun errorDialogRetryClick(type: Int) {
        ErrorDialogFragment.cancel(childFragmentManager)
        retryDialogRetryAction?.let {
            it.invoke()
        }
    }
}