package com.stenleone.rockpaperscissors.ui.fragments.signInOrRegister

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentRegisterBinding
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
                { viewModel?.register() }, lifecycleScope
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

        }
    }

}