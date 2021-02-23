package com.stenleone.rockpaperscissors.ui.dialogs.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.stenleone.stanleysfilm.util.extencial.hideKeyboard

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {

    protected lateinit var binding: T
    protected abstract var layId: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideKeyboard()
        setup(savedInstanceState)
    }

    protected abstract fun setup(savedInstanceState: Bundle?)
}