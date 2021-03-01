package com.stenleone.rockpaperscissors.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogGameChoserBinding
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import com.stenleone.rockpaperscissors.ui.fragments.createRoom.HostCreateRoomFragment
import com.stenleone.rockpaperscissors.ui.fragments.main.MainFragment
import com.stenleone.rockpaperscissors.ui.fragments.player.PlayerFragment
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
                    (requireActivity() as MainActivity).addFragment(requireActivity().supportFragmentManager.findFragmentByTag(MainFragment.TAG), HostCreateRoomFragment(), HostCreateRoomFragment.TAG)
                    dialog?.dismiss()
                }, lifecycleScope
            )
            joinButton.throttleClicks(
                {
                    (requireActivity() as MainActivity).addFragment(requireActivity().supportFragmentManager.findFragmentByTag(MainFragment.TAG), PlayerFragment(), PlayerFragment.TAG)
                    dialog?.dismiss()
                }, lifecycleScope
            )
        }
    }

}