package com.stenleone.rockpaperscissors.ui.dialogs.enterPassRoom

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogRoomPassBinding
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import com.stenleone.rockpaperscissors.ui.fragments.findRoom.FindRoomFragment
import com.stenleone.rockpaperscissors.ui.fragments.player.PlayerFragment
import com.stenleone.rockpaperscissors.ui.fragments.findRoom.ConnectToRoomViewModel
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomPassDialogFragment(override var layId: Int = R.layout.dialog_room_pass) : BaseDialogFragment<DialogRoomPassBinding>() {

    companion object {

        const val TAG = "RoomPassDialogFragment"
        const val TITLE = "title"
        const val ROOM = "room"

        fun show(fragmentManager: FragmentManager, title: String?, room: Room) {

            RoomPassDialogFragment().also {
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    val bundle = Bundle().also {
                        it.putString(TITLE, title)
                        it.putParcelable(ROOM, room)
                    }
                    it.arguments = bundle
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

    private val viewModelConnectTo: ConnectToRoomViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            title = arguments?.getString(TITLE)
        }
        setupClicks()
        setupViewModelCallBack()
    }

    private fun setupClicks() {
        binding.apply {
            okButton.throttleClicks(
                {
                    val password = passEdit.text.toString()
                    if (!password.isNullOrEmpty()) {
                        arguments?.getParcelable<Room>(ROOM)?.let {
                            viewModelConnectTo.connect(it, password)
                        }
                    }

                }, lifecycleScope
            )
        }
    }

    private fun setupViewModelCallBack() {
        viewModelConnectTo.apply {
            connected.observe(viewLifecycleOwner) {
                val bundle = Bundle().also {
                    it.putParcelable(PlayerFragment.ROOM, arguments?.getParcelable<Room>(ROOM))
                }
                (requireActivity() as MainActivity).addFragment(requireActivity().supportFragmentManager.findFragmentByTag(FindRoomFragment.TAG), PlayerFragment(), PlayerFragment.TAG, bundle)
                dialog?.dismiss()
            }
            error.observe(viewLifecycleOwner) {

            }
            inProgress.observe(viewLifecycleOwner) {
                if (it) {
                    LoadingDialogFragment.show(childFragmentManager)
                } else {
                    LoadingDialogFragment.cancel(childFragmentManager)
                }
            }
        }
    }

}