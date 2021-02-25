package com.stenleone.rockpaperscissors.ui.dialogs

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogInfoBinding
import com.stenleone.rockpaperscissors.databinding.DialogLoadingBinding
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingDialogFragment(override var layId: Int = R.layout.dialog_loading) : BaseDialogFragment<DialogLoadingBinding>() {

    companion object {

        const val TAG = "LoadingDialogFragment"


        fun show (fragmentManager: FragmentManager) {

            LoadingDialogFragment().also {
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
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0))
    }

}