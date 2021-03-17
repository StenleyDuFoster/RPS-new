package com.stenleone.rockpaperscissors.ui.fragments.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.google.firebase.auth.FirebaseAuth
import com.stenleone.rockpaperscissors.BuildConfig
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentMainBinding
import com.stenleone.rockpaperscissors.managers.config.RemoteConfigManager
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.activitys.AuthActivity
import com.stenleone.rockpaperscissors.ui.dialogs.GameChoserDialogFragment
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.ui.fragments.profile.ProfileFragment
import com.stenleone.rockpaperscissors.utils.constants.RemoteConfig
import com.stenleone.rockpaperscissors.utils.extencial.share
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment(override var layId: Int = R.layout.fragment_main) : BaseFragment<FragmentMainBinding>() {

    companion object {
        const val TAG = "MainFragment"
    }

    @Inject
    lateinit var remoteConfig: RemoteConfigManager

    override fun setup(savedInstanceState: Bundle?) {

        setupBottomSheetFragment()
        setupClicks()
        setupRemoteConfig()
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
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }, lifecycleScope
            )
            profile.throttleClicks(
                {
                    (requireActivity() as MainActivity).addFragment(this@MainFragment, ProfileFragment(), ProfileFragment.TAG)
                }, lifecycleScope
            )
            share.throttleClicks(
                {
                    requireActivity().share(getString(R.string.share_via), getString(R.string.app_in_market))
                }, lifecycleScope
            )
            rateApp.throttleClicks(
                {
                    val manager = if (BuildConfig.DEBUG) {
                        FakeReviewManager(requireContext())
                    } else {
                        ReviewManagerFactory.create(requireContext())
                    }
                    val request = manager.requestReviewFlow()
                    request.addOnCompleteListener { request ->
                        if (request.isSuccessful) {
                            val reviewInfo = request.result
                            manager.launchReviewFlow(requireActivity(), reviewInfo).addOnSuccessListener { }
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }, lifecycleScope
            )
            supportApp.throttleClicks(
                {

                }, lifecycleScope
            )
            playButton.throttleClicks(
                {
                    GameChoserDialogFragment.show(childFragmentManager)
                }, lifecycleScope
            )
        }
    }

    private fun setupRemoteConfig() {
        remoteConfig.apply {
            binding.apply {
                if (!getBoolean(RemoteConfig.SHOW_RATE_APP)) {
                    rateApp.visibility = View.GONE
                }
                if (!getBoolean(RemoteConfig.SHOW_SUPPORT_DEV)) {
                    supportApp.visibility = View.GONE
                }
            }
        }
    }
}