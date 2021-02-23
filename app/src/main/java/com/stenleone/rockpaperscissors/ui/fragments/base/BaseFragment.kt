package com.stenleone.rockpaperscissors.ui.fragments.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.stenleone.stanleysfilm.util.extencial.hideKeyboard

abstract class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().hideKeyboard()
    }
}