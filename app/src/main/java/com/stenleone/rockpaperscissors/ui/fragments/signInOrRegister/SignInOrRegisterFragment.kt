package com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentRegisterBinding
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogCallBack
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.error.ErrorDialogFragment.Companion.ERROR_DIALOG_CONNECTION_ACTION
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInOrRegisterFragment(override var layId: Int = R.layout.fragment_register) : BaseFragment<FragmentRegisterBinding>(), ErrorDialogCallBack {

    companion object {
        const val TAG = "SignInOrRegisterFragment"
    }

    private val viewModel: SignInOrRegisterViewModel by viewModels()
    private var retryDialogRetryAction: (() -> Unit)? = null

    override fun setup(savedInstanceState: Bundle?) {

        setupBinding()
        setupViewModel()
        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            registerButton.throttleClicks(
                {
                    if (Patterns.EMAIL_ADDRESS.matcher(emailEdit.text).matches()) {
                        if (passEdit.text?.length ?: 0 > 6) {
                            viewModel?.register()
                        } else {
                            passEdit.setError(getString(R.string.pass_length_error))
                        }
                    } else {
                        emailEdit.setError(getString(R.string.email_format_error))
                    }

                }, lifecycleScope
            )
        }
    }

    private fun setupBinding() {
        binding.apply {
            lifecycleOwner = this@SignInOrRegisterFragment
            viewModel = this@SignInOrRegisterFragment.viewModel
        }
    }

    private fun setupViewModel() {
        viewModel.apply {
            emailError.observe(viewLifecycleOwner) {
                binding.emailEdit.setError(it)
            }
            passwordError.observe(viewLifecycleOwner) {
                binding.passEdit.setError(it)
            }
            inProgress.observe(viewLifecycleOwner) {
                if (it) {
                    LoadingDialogFragment.show(childFragmentManager)
                } else {
                    LoadingDialogFragment.cancel(childFragmentManager)
                }
            }
            registerResult.observe(viewLifecycleOwner) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }
            connectionError.observe(viewLifecycleOwner) {
                ErrorDialogFragment.showStandart(
                    childFragmentManager,
                    getString(R.string.connection_text),
                    null,
                    getString(R.string.ok),
                    getString(R.string.retry),
                    ERROR_DIALOG_CONNECTION_ACTION
                )
                retryDialogRetryAction = it
            }
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