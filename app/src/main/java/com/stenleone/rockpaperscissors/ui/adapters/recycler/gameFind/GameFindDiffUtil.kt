package com.stenleone.rockpaperscissors.ui.adapters.recycler.gameFind

import androidx.recyclerview.widget.DiffUtil
import com.stenleone.rockpaperscissors.model.network.Room

class GameFindDiffUtil(
    private var oldList: ArrayList<Room> = arrayListOf(),
    private var newList: ArrayList<Room> = arrayListOf()
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name &&
                oldList[oldItemPosition].playerCount == newList[newItemPosition].playerCount &&
                oldList[oldItemPosition].players.size == newList[newItemPosition].players.size &&
                oldList[oldItemPosition].round == newList[newItemPosition].round &&
                oldList[oldItemPosition].games == newList[newItemPosition].games
    }

}