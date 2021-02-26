package com.stenleone.rockpaperscissors.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentMainBinding
import com.stenleone.rockpaperscissors.ui.activitys.RegisterActivity
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks

class MainFragment(override var layId: Int = R.layout.fragment_main) : BaseFragment<FragmentMainBinding>() {

    companion object {
        const val TAG = "MainFragment"
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupBottomSheetFragment()
        setupClicks()
    }

    private fun setupBottomSheetFragment() {
        val bottomFragment = BottomSheetDialogFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.bottomSheetBehavior, bottomFragment)
            .commit()

        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheetBehavior)
        BottomSheetBehavior.from(binding.bottomSheetBehavior).state = BottomSheetBehavior.STATE_COLLAPSED
        binding.grayLay.isFocusable = false
        binding.grayLay.isClickable = false

        bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.grayLay.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.grayLay.isFocusable = false
                        binding.grayLay.isClickable = true
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.grayLay.isFocusable = false
                        binding.grayLay.isClickable = false
                    }
                }
            }
        })
    }

    private fun setupClicks() {
        binding.apply {
            playButton.throttleClicks(
                {

                }, lifecycleScope
            )
            grayLay.throttleClicks(
                {
                    BottomSheetBehavior.from(binding.bottomSheetBehavior).state = BottomSheetBehavior.STATE_COLLAPSED
                }, lifecycleScope
            )
            dotsLay.throttleClicks(
                {
                    if (BottomSheetBehavior.from(binding.bottomSheetBehavior).state == BottomSheetBehavior.STATE_EXPANDED) {
                        BottomSheetBehavior.from(binding.bottomSheetBehavior).state = BottomSheetBehavior.STATE_COLLAPSED
                    } else {
                        BottomSheetBehavior.from(binding.bottomSheetBehavior).state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }, lifecycleScope
            )
            logOut.throttleClicks(
                {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), RegisterActivity::class.java))
                    requireActivity().finish()
                }, lifecycleScope
            )
        }
    }
}