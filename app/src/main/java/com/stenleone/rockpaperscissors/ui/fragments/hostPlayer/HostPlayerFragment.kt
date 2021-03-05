package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.work.*
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import com.stenleone.rockpaperscissors.ui.adapters.recycler.GamersAdapter
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.utils.constants.RPS
import com.stenleone.rockpaperscissors.workers.DestroyRoomWorker
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class HostPlayerFragment(override var layId: Int = R.layout.fragment_player) : BaseFragment<FragmentPlayerBinding>() {

    companion object {
        const val TAG = "HostPlayerFragment"
    }

    private val viewModel: HostPlayerViewModel by viewModels()
    @Inject
    lateinit var gamerAdapter: GamersAdapter

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModel()
        setupClicks()
    }

    private fun setupViewModel() {
        viewModel.apply {
            arguments?.getParcelable<Room>(BaseActivity.DATA)?.let {
                setupRoom(it)
                binding.apply {
                    title.text = it.name
                    gameRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
                    gameRecycler.adapter = gamerAdapter
                }
            }
            roomData.observe(viewLifecycleOwner) {

                if (it.round > it.games) {
                    Toast.makeText(requireContext(), "finish game", Toast.LENGTH_SHORT).show()
                }
                binding.roundText.text = getString(R.string.round_text, it.round.toString(), it.games.toString())

                gamerAdapter.listItems.clear()
                val players = ArrayList(it.players.values)
                players.removeFirstOrNull()
                gamerAdapter.listItems.addAll(players)
                gamerAdapter.notifyDataSetChanged()
            }
            error.observe(viewLifecycleOwner) {

            }
            lockButtons.observe(viewLifecycleOwner) {
                binding.apply {
                    rockButton.isClickable = true
                    paperButton.isClickable = true
                    scissorsButton.isClickable = true
                }
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
            rockButton.throttleClicks(
                {
                    createStep(RPS.ROCK)
                }, lifecycleScope
            )
            scissorsButton.throttleClicks(
                {
                    createStep(RPS.SCISSORS)
                }, lifecycleScope
            )
            paperButton.throttleClicks(
                {
                    createStep(RPS.PAPER)
                }, lifecycleScope
            )
      }
    }

    private fun createStep(step: RPS) {
        binding.apply {
            rockButton.isClickable = false
            paperButton.isClickable = false
            scissorsButton.isClickable = false
        }
        viewModel.createStep(step)
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