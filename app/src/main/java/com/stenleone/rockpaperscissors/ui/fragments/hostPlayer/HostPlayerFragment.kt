package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.work.*
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.workers.DestroyRoomWorker
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HostPlayerFragment(override var layId: Int = R.layout.fragment_player) : BaseFragment<FragmentPlayerBinding>() {

    companion object {
        const val TAG = "HostPlayerFragment"
    }

    private val viewModel: HostPlayerViewModel by viewModels()

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModel()
        setupClicks()
    }

    private fun setupViewModel() {
        viewModel.apply {
            arguments?.getParcelable<Room>(BaseActivity.DATA)?.let {
                setupRoom(it)
            }
            roomData.observe(viewLifecycleOwner) {

            }
            error.observe(viewLifecycleOwner) {

            }
        }
    }

    private fun setupClicks() {
        binding.apply {
            backButton.throttleClicks(
                {
                    requireActivity().onBackPressed()
                }, lifecycleScope
            )
        }
    }

    private fun removeRoom(withDelay: Boolean = false) {
        viewModel.roomData.value?.let { room ->

            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val data = Data.Builder().also {
                it.putString(DestroyRoomWorker.ROOM_NAME, room.name)
            }

            val testWorkRequest = OneTimeWorkRequest.Builder(DestroyRoomWorker::class.java)
                .setInputData(data.build())
                .setConstraints(constraints)
                .addTag(DestroyRoomWorker.TAG)

            if (withDelay) {
                testWorkRequest.setInitialDelay(15L, TimeUnit.MINUTES)
            }

            WorkManager
                .getInstance(requireContext())
                .enqueue(testWorkRequest.build())
        }

    }

    override fun onPause() {
        removeRoom(true)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        WorkManager
            .getInstance(requireContext())
            .cancelAllWorkByTag(DestroyRoomWorker.TAG)
    }

    override fun onDestroy() {
        removeRoom()
        super.onDestroy()
    }
}