package com.stenleone.rockpaperscissors.ui.fragments.findRoom

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.R
import com.stenleone.rockpaperscissors.databinding.FragmentFindRoomBinding
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.adapters.recycler.GameFindAdapter
import com.stenleone.rockpaperscissors.ui.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FindRoomFragment(override var layId: Int = R.layout.fragment_find_room) : BaseFragment<FragmentFindRoomBinding>() {

    companion object {
        const val TAG = "FindRoomFragment"
    }

    private val viewModel: FindRoomViewModel by viewModels()

    @Inject
    lateinit var gameFindAdapter: GameFindAdapter

    override fun setup(savedInstanceState: Bundle?) {

        setupViewModelCallBack()
        setupRecyclerView()
    }

    private fun setupViewModelCallBack() {
        viewModel.listRooms.observe(viewLifecycleOwner) {
            val rooms = arrayListOf<Room>()

            it.o?.let { it1 -> rooms.add(it1) }
            gameFindAdapter.listItems.clear()
            gameFindAdapter.listItems.addAll(rooms)
            gameFindAdapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        binding.apply {
            gameRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            gameRecycler.adapter = gameFindAdapter
        }
    }
}