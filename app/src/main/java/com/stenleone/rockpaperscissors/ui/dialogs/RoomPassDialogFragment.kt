package com.stenleone.rockpaperscissors.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogInfoBinding
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomPassDialogFragment(override var layId: Int = R.layout.dialog_info) : BaseDialogFragment<DialogInfoBinding>() {

    companion object {

        const val TAG = "InfoDialogFragment"
        const val TITLE = "title"
        const val TEXT = "text"
        const val WITH_ACTION = "with_action"

        fun show(fragmentManager: FragmentManager, title: String?, text: String?, withAction: Int? = null) {

            RoomPassDialogFragment().also {
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    val bundle = Bundle().also {
                        it.putString(TITLE, title)
                        it.putString(TEXT, text)
                        if (withAction != null) {
                            it.putInt(WITH_ACTION, withAction)
                        }
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

    override fun setup(savedInstanceState: Bundle?) {
        binding.apply {
            title = arguments?.getString(TITLE)
            text = arguments?.getString(TEXT)
        }
        setupClicks()
    }

    private fun setupClicks() {
        binding.apply {
            okButton.throttleClicks(
                {
                    val action = arguments?.getInt(WITH_ACTION)
                    if (action != null && parentFragment is Callback)  {
                        (parentFragment as Callback).infoDialogOkClick(action)
                    }
                    dialog?.dismiss()
                }, lifecycleScope
            )
        }
    }

    interface Callback {
        fun infoDialogOkClick(type: Int)
    }

}