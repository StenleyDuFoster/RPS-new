package com.stenleone.rockpaperscissors.ui.dialogs.error

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogErrorBinding
import com.stenleone.rockpaperscissors.databinding.DialogInfoBinding
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialogFragment(override var layId: Int = R.layout.dialog_error) : BaseDialogFragment<DialogErrorBinding>() {

    companion object {
        const val TAG = "ErrorDialogFragment"
        const val ERROR_DIALOG_CONNECTION_ACTION = 0
        const val ERROR_DIALOG_UNKNOWN_ACTION = 1

        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val OK_BUTTON = "okButton"
        private const val RETRY_BUTTON = "retryButton"
        private const val CRITICAL = "critical"
        private const val ACTION = "action"

        fun showCritical(fragmentManager: FragmentManager, title: String, message: String?, okButton: String?, retryButton: String?, action: Int? = null) {
            ErrorDialogFragment().also { f ->
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    val bundle = Bundle().also {
                        it.putString(TITLE, title)
                        it.putString(MESSAGE, message)
                        it.putString(OK_BUTTON, okButton)
                        it.putString(RETRY_BUTTON, retryButton)
                        it.putBoolean(CRITICAL, true)
                        action?.let { notNullAction ->
                            it.putInt(ACTION, notNullAction)
                        }
                    }
                    f.arguments = bundle
                    f.show(fragmentManager, TAG)
                }
            }
        }

        fun showStandart(fragmentManager: FragmentManager, title: String, message: String?, okButton: String?, retryButton: String?, action: Int? = null) {
            ErrorDialogFragment().also { f ->
                if (fragmentManager.findFragmentByTag(TAG) == null) {
                    val bundle = Bundle().also {
                        it.putString(TITLE, title)
                        it.putString(MESSAGE, message)
                        it.putString(OK_BUTTON, okButton)
                        it.putString(RETRY_BUTTON, retryButton)
                        it.putBoolean(CRITICAL, false)
                        action?.let { notNullAction ->
                            it.putInt(ACTION, notNullAction)
                        }
                    }
                    f.arguments = bundle
                    f.show(fragmentManager, TAG)
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
        arguments?.getBoolean(CRITICAL)?.let {
            if (it) {
                isCancelable = false
            }
        }
        setupViews()
    }

    private fun setupViews() {
        binding.apply {

            title = arguments?.getString(TITLE)
            text = arguments?.getString(MESSAGE)
            okButtonText = arguments?.getString(OK_BUTTON)
            retryButtonText = arguments?.getString(RETRY_BUTTON)

            okButton.throttleClicks(
                {
                    arguments?.getInt(ACTION)?.let { action ->
                        (parentFragment as? ErrorDialogCallBack?)?.errorDialogOkClick(action)
                    }
                    arguments?.getBoolean(CRITICAL)?.let {
                        if (!it) {
                            dialog?.dismiss()
                        }
                    }
                }, lifecycleScope
            )
            retryButton.throttleClicks(
                {
                    arguments?.getInt(ACTION)?.let { action ->
                        (parentFragment as? ErrorDialogCallBack?)?.errorDialogRetryClick(action)
                    }
                    arguments?.getBoolean(CRITICAL)?.let {
                        if (!it) {
                            dialog?.dismiss()
                        }
                    }
                }, lifecycleScope
            )
        }
    }
}