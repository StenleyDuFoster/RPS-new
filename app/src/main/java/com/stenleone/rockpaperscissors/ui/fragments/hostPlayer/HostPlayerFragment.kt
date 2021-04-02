package com.stenleone.rockpaperscissors.ui.fragments.hostPlayer

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentPlayerBinding
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.services.RoomControlService
import com.stenleone.rockpaperscissors.ui.activitys.base.BaseActivity
import com.stenleone.rockpaperscissors.ui.adapters.recycler.gameLay.GamersAdapter
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.utils.constants.RPS
import com.stenleone.stanleysfilm.util.extencial.getOrientation
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
                RoomControlService.setupHost(requireActivity(), it.name)
                setupRoom(it)
                binding.apply {
                    gamerAdapter.size = it.playerCount - 1
                    title.text = it.name
                    val spanCount = if (requireActivity().getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
                        3
                    } else {
                        6
                    }
                    gameRecycler.layoutManager = GridLayoutManager(requireContext(), spanCount)
                    gameRecycler.adapter = gamerAdapter
                }
            }
            roomData.observe(viewLifecycleOwner) {

                if (it.round > it.games) {
                    Toast.makeText(requireContext(), "finish game", Toast.LENGTH_SHORT).show()
                }
                binding.roundText.text = getString(R.string.round_text, it.round.toString(), it.games.toString())

                val players = ArrayList(it.players.values)
                var myIndex: Int? = null
                players.forEachIndexed { index, item ->
                    if (item.name == viewModel.name) {
                        myIndex = index
                    }
                }
                myIndex?.let {
                    players.removeAt(it)
                }

                gamerAdapter.diffUpdateList(players)
            }
            roomData.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Round ${it.round} start", Toast.LENGTH_SHORT).show()
                gamerAdapter.diffUpdateUserSteps(false)

                binding.apply {

                    paperButton.visibility = View.VISIBLE
                    scissorsButton.visibility = View.VISIBLE
                    rockButton.visibility = View.VISIBLE

                    rockButton.isClickable = true
                    paperButton.isClickable = true
                    scissorsButton.isClickable = true
                }

                gamerAdapter.diffUpdateRound(it.round)
            }
            error.observe(viewLifecycleOwner) {

            }
            lockButtons.observe(viewLifecycleOwner) {
                binding.apply {
//                    gamerAdapter.diffUpdateUserSteps(false)


                }
            }

        }
    }



    private fun setupClicks() {
        binding.apply {
            rockButton.isClickable = true
            paperButton.isClickable = true
            scissorsButton.isClickable = true
            backButton.throttleClicks(
                {
                    requireActivity().onBackPressed()
                }, lifecycleScope
            )
            rockButton.throttleClicks(
                {
                    paperButton.visibility = View.GONE
                    scissorsButton.visibility = View.GONE
                    createStep(RPS.ROCK)
                }, lifecycleScope
            )
            scissorsButton.throttleClicks(
                {
                    rockButton.visibility = View.GONE
                    paperButton.visibility = View.GONE
                    createStep(RPS.SCISSORS)
                }, lifecycleScope
            )
            paperButton.throttleClicks(
                {
                    rockButton.visibility = View.GONE
                    scissorsButton.visibility = View.GONE
                    createStep(RPS.PAPER)
                }, lifecycleScope
            )
        }
    }

    private fun createStep(step: RPS) {
        binding.apply {

            gamerAdapter.diffUpdateUserSteps(true)
            rockButton.isClickable = false
            paperButton.isClickable = false
            scissorsButton.isClickable = false
        }
        viewModel.createStep(step)
    }

    override fun onDestroy() {
        Log.v("112233", "Host destroy")
        RoomControlService.destroyHost(requireActivity())

        super.onDestroy()
    }
}