package com.stenleone.rockpaperscissors.ui.fragments.createRoom

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentCreateRoomBinding
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.ui.fragments.hostPlayer.HostPlayerFragment
import com.stenleone.rockpaperscissors.ui.fragments.singleRoom.SinglePlayerFragment
import com.stenleone.rockpaperscissors.utils.TopMenuController
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HostCreateRoomFragment(override var layId: Int = R.layout.fragment_create_room) : BaseFragment<FragmentCreateRoomBinding>() {

    companion object {
        const val TAG = "HostCreateRoomFragment"
    }

    private var isOnline: Boolean? = null
    private var gameCount: String? = null
    private var gamersCount: String? = null

    override fun setup(savedInstanceState: Bundle?) {

        setupView()
        setupClicks()
//        TopMenuController(binding.toolBar)
    }

    private fun setupView() {
        binding.apply {
            val gameAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
            gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playerGameSpinner.adapter = gameAdapter
            playerGameSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    gameCount = (position + 2).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            })

            val gamersAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(2, 3, 4, 5, 6, 7, 8, 9, 10))
            gamersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            playerCountSpinner.adapter = gamersAdapter
            playerCountSpinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    gamersCount = (position + 2).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            })
        }
    }

    private fun setupClicks() {
        binding.apply {
            toolBar.backButton.throttleClicks(
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
            onlineText.throttleClicks(
                {
                    if (isOnline == false || isOnline == null) {
                        (onlineText.background as TransitionDrawable).startTransition(400)
                        if (isOnline == false) {
                            (offlineText.background as TransitionDrawable).reverseTransition(400)
                        }
                        isOnline = true
                    } else {
                        (onlineText.background as TransitionDrawable).reverseTransition(400)
                        isOnline = null
                    }
                }, lifecycleScope
            )
            offlineText.throttleClicks(
                {
                    if (isOnline == true || isOnline == null) {
                        (offlineText.background as TransitionDrawable).startTransition(400)
                        if (isOnline == true) {
                            (onlineText.background as TransitionDrawable).reverseTransition(400)
                        }
                        isOnline = false
                    } else {
                        (offlineText.background as TransitionDrawable).reverseTransition(400)
                        isOnline = null
                    }
                }, lifecycleScope
            )
            createButton.throttleClicks(
                {
                    if (validate()) {
                        val roomInfo = Room(
                            nameEdit.text.toString(),
                            if (addPassCheckBox.isChecked) passEdit.text.toString() else null,
                            gamersCount?.toIntOrNull() ?: 2,
                            gameCount?.toIntOrNull() ?: 1,
                            1,
                            Date(System.currentTimeMillis()).toString(),
                            mapOf()
                        )
                        if (isOnline == true) {
                            (requireActivity() as MainActivity).addFragment(this@HostCreateRoomFragment, HostPlayerFragment(), HostPlayerFragment.TAG, roomInfo)
                        } else if (isOnline == false) {
                            (requireActivity() as MainActivity).addFragment(this@HostCreateRoomFragment, SinglePlayerFragment(), SinglePlayerFragment.TAG, roomInfo)
                        } else {
                            Toast.makeText(requireContext(), getString(R.string.choise_game_parameters, getString(R.string.online), getString(R.string.offline)), Toast.LENGTH_SHORT).show()
                        }
                    }
                }, lifecycleScope
            )
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        binding.apply {
            if (nameEdit.text.toString().length == 0) {
                nameEdit.error = getString(R.string.edit_text_must_not_empty)
                isValid = false
            }
            if (addPassCheckBox.isChecked && passEdit.text.toString().length == 0) {
                passEdit.error = getString(R.string.edit_text_must_not_empty)
                isValid = false
            }
        }

        return isValid
    }

}