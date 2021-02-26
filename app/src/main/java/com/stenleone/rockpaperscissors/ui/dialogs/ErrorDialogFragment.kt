package com.stenleone.rockpaperscissors.ui.dialogs

import android.os.Bundle
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.DialogInfoBinding
import com.stenleone.rockpaperscissors.ui.dialogs.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialogFragment(override var layId: Int = R.layout.dialog_info) : BaseDialogFragment<DialogInfoBinding>() {

    override fun setup(savedInstanceState: Bundle?) {
        isCancelable = false
    }

}