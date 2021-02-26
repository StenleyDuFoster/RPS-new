package com.stenleone.rockpaperscissors.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogGameChoserBinding
import com.stenleone.rockpaperscissors.databinding.DialogInfoBinding
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameChoserDialogFragment(override var layId: Int = R.layout.dialog_game_choser) : BaseDialogFragment<DialogGameChoserBinding>() {

    companion object {

        const val TAG = "GameChoserDialogFragment"

        fun show(fragmentManager: FragmentManager) {

            GameChoserDialogFragment().also {
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    it.show(fragmentManager, TAG)
                }
            }
        }

        fun cancel(fragmentManager: FragmentManager) {
            fragmentManager.findFragmentByTag(TAG)?.let {
                (it as DialogFragment).dismiss()
            }
        }
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            createButton.throttleClicks(
                {

                }, lifecycleScope
            )
            joinButton.throttleClicks(
                {

                }, lifecycleScope
            )
        }
    }

}