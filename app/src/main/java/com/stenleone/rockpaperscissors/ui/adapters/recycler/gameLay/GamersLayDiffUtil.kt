package com.stenleone.rockpaperscissors.ui.adapters.recycler.gameLay

import androidx.recyclerview.widget.DiffUtil
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.model.network.Room

class GamersLayDiffUtil(
    private var oldList: ArrayList<GameUser> = arrayListOf(),
    private var newList: ArrayList<GameUser> = arrayListOf(),
    private var size: Int,
    private var round: Int?,
    private var userStep: Boolean,
    private var newSize: Int,
    private var newRound: Int?,
    private var newUserStep: Boolean
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
                oldList[oldItemPosition].win == newList[newItemPosition].win &&
                oldList[oldItemPosition].steps.size == newList[newItemPosition].steps.size &&
                oldList[oldItemPosition].lose == newList[newItemPosition].lose &&
                size == newSize &&
                round == newRound &&
                userStep == newUserStep
    }

}