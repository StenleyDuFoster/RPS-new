package com.stenleone.rockpaperscissors.ui.fragments.findRoom

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentFindRoomBinding
import com.stenleone.rockpaperscissors.interfaces.SimpleClickListener
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.activitys.MainActivity
import com.stenleone.rockpaperscissors.ui.adapters.recycler.GameFindAdapter
import com.stenleone.rockpaperscissors.ui.dialogs.InfoDialogFragment
import com.stenleone.rockpaperscissors.ui.dialogs.LoadingDialogFragment
import com.stenleone.rockpaperscissors.viewModel.ConnectToRoomViewModel
import com.stenleone.rockpaperscissors.ui.dialogs.enterPassRoom.RoomPassDialogFragment
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import com.stenleone.rockpaperscissors.ui.fragments.player.PlayerFragment
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class FindRoomFragment(override var layId: Int = R.layout.fragment_find_room) : BaseFragment<FragmentFindRoomBinding>(), InfoDialogFragment.Callback {

    companion object {
        const val TAG = "FindRoomFragment"
        const val INFO_DIALOG_OPEN_ROOM_ACTION = 0
    }

    private val viewModel: FindRoomViewModel by viewModels()
    private val connectedViewModel: ConnectToRoomViewModel by viewModels()
    private var lastClickRoom: Room? = null

    @Inject
    lateinit var gameFindAdapter: GameFindAdapter

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModelCallBack()
        setupRecyclerView()
        setupClicks()
    }

    private fun setupViewModelCallBack() {
        viewModel.listRooms.observe(viewLifecycleOwner) {
            val rooms = arrayListOf<Room>()

            rooms.addAll(it)

            val startPosition = gameFindAdapter.listItems.size

            gameFindAdapter.listItems.clear()
            gameFindAdapter.listItems.addAll(rooms)

            when {
                startPosition == 0 -> {
                    gameFindAdapter.notifyItemRangeInserted(0, gameFindAdapter.listItems.size)
                }
                startPosition > gameFindAdapter.listItems.size -> {
                    gameFindAdapter.notifyItemRangeRemoved(startPosition, startPosition - gameFindAdapter.listItems.size)
                }
                startPosition < gameFindAdapter.listItems.size -> {
                    gameFindAdapter.notifyItemRangeRemoved(gameFindAdapter.listItems.size - startPosition, gameFindAdapter.listItems.size)
                }
                else -> {
                    gameFindAdapter.notifyDataSetChanged()
                }
            }
        }

        connectedViewModel.connected.observe(viewLifecycleOwner) { connected ->
            if (connected) {
                lastClickRoom?.let { room ->
                    val bundle = Bundle().also {
                        it.putParcelable(PlayerFragment.ROOM, room)
                    }
                    (requireActivity() as MainActivity).addFragment(this, PlayerFragment(), PlayerFragment.TAG, bundle)
                }
            }
        }
        connectedViewModel.inProgress.observe(viewLifecycleOwner) {
            if (it) {
                LoadingDialogFragment.show(childFragmentManager)
            } else {
                LoadingDialogFragment.cancel(childFragmentManager)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            gameFindAdapter.clickListener = object : SimpleClickListener {

                @SuppressLint("SimpleDateFormat")
                override fun click(item: Parcelable) {
                    if (item is Room) {

                        lastClickRoom = item

                        val calendar = Calendar.getInstance()
                        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy")
                        calendar.time = try {
                            sdf.parse(item.date_create)
                        } catch (e: Exception) {
                            null
                        }
                        calendar.add(Calendar.HOUR, 1)

                        when {
                            item.playerCount <= item.players.values.size -> {
                                InfoDialogFragment.show(childFragmentManager, getString(R.string.room_connected_failed), getString(R.string.room_no_actual_player_count, item.name))
                            }
                            calendar.timeInMillis < Calendar.getInstance().timeInMillis -> {
                                InfoDialogFragment.show(childFragmentManager, getString(R.string.advice), getString(R.string.room_long_time_create), INFO_DIALOG_OPEN_ROOM_ACTION)
                            }
                            else -> {
                                goToRoom()
                            }
                        }

                    }
                }

            }
            gameRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            gameRecycler.adapter = gameFindAdapter
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

    private fun goToRoom() {
        lastClickRoom?.let {
            if (it.password != null) {
                RoomPassDialogFragment.show(childFragmentManager, getString(R.string.room_defendended_by_pass, it.name), it)
            } else {
                connectedViewModel.connect(it)
            }
        }
    }

    override fun infoDialogOkClick(type: Int) {
        when (type) {
            INFO_DIALOG_OPEN_ROOM_ACTION -> {
                goToRoom()
            }
        }
    }
}