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
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInOrRegisterFragment(override var layId: Int = R.layout.fragment_register) : BaseFragment<FragmentRegisterBinding>() {

    companion object {
        const val TAG = "SignInOrRegisterFragment"
    }

    private val viewModel: SignInOrRegisterViewModel by viewModels()

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
                            passEdit.setError("Пароль должен иметь более 6 символов")
                        }
                    } else {
                        emailEdit.setError("Почти должна быть в формате aaa@example.com")
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
        }
    }

}