package com.stenleone.rockpaperscissors.ui.fragments.createRoom

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentCreateRoomBinding
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostCreateRoomFragment(override var layId: Int = R.layout.fragment_create_room) : BaseFragment<FragmentCreateRoomBinding>() {

    companion object {
        const val TAG = "HostCreateRoomFragment"
    }

    override fun setup(savedInstanceState: Bundle?) {

        setupView()
        setupClicks()
    }

    private fun setupView() {
        binding.apply {
            val gamersAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(2, 3, 4, 5, 6, 7, 8, 9, 10))
            gamersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playerCountSpinner.adapter = gamersAdapter
        }
    }

    private fun setupClicks() {
        binding.apply {
            backButton.throttleClicks(
                {
                    requireActivity().onBackPressed()
                }, lifecycleScope
            )
            addPassCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    pass.visibility = View.VISIBLE
                } else {
                    pass.visibility = View.GONE
                }
            }
        }
    }

}