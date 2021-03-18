package com.stenleone.rockpaperscissors.ui.adapters.recycler.gameLay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.databinding.ItemGameLayBinding
import com.stenleone.rockpaperscissors.interfaces.RecyclerHolder
import com.stenleone.rockpaperscissors.interfaces.SimpleClickListener
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.rockpaperscissors.ui.adapters.recycler.base.BaseRecycler
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import javax.inject.Inject

class GamersAdapter @Inject constructor() : BaseRecycler<GameUser>() {

    companion object {
        const val PLAYER_TYPE = 0
        const val EMPTY_TYPE = 1
    }

    lateinit var clickListener: SimpleClickListener
    var size = 0
    var round: Int? = null
    var userStep = false

    fun diffUpdateList(gamers: ArrayList<GameUser>) {

        val diffResult = DiffUtil.calculateDiff(
            GamersLayDiffUtil(
                this.listItems,
                gamers,
                this.size,
                this.round,
                this.userStep,
                this.size,
                this.round,
                this.userStep
            )
        )

        listItems.clear()
        listItems.addAll(gamers)

        diffResult.dispatchUpdatesTo(this)
    }

    fun diffUpdateRound(round: Int) {

        val diffResult = DiffUtil.calculateDiff(
            GamersLayDiffUtil(
                this.listItems,
                this.listItems,
                this.size,
                this.round,
                this.userStep,
                this.size,
                round,
                this.userStep
            )
        )
        this.round = round

        diffResult.dispatchUpdatesTo(this)
    }

    fun diffUpdateUserSteps(userStep: Boolean) {

        val diffResult = DiffUtil.calculateDiff(
            GamersLayDiffUtil(
                this.listItems,
                this.listItems,
                this.size,
                this.round,
                this.userStep,
                this.size,
                this.round,
                userStep
            )
        )

        this.userStep = userStep

        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            PLAYER_TYPE -> {
                PlayerGameLay(ItemGameLayBinding.inflate(LayoutInflater.from(parent.context)))
            }
            else -> {
                EmptyGameLay(ItemGameLayBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= listItems.size) {
            EMPTY_TYPE
        } else {
            PLAYER_TYPE
        }
    }

    inner class PlayerGameLay(val binding: ItemGameLayBinding) : RecyclerView.ViewHolder(binding.root), RecyclerHolder {

        override fun bind() {
            binding.apply {

                gamer = listItems[adapterPosition]
                round = this@GamersAdapter.round
                userStep = this@GamersAdapter.userStep

                binding.playerLay.throttleClicks(
                    {
                        if (this@GamersAdapter::clickListener.isInitialized) {
                            clickListener.click(listItems[adapterPosition])
                        }
                    }, recyclerScope
                )
            }
        }

        override fun unBind() {

        }
    }

    inner class EmptyGameLay(val binding: ItemGameLayBinding) : RecyclerView.ViewHolder(binding.root), RecyclerHolder {

        override fun bind() {
            binding.apply {

                binding.playerLay.throttleClicks(
                    {
                        if (this@GamersAdapter::clickListener.isInitialized) {
                            clickListener.click(listItems[adapterPosition])
                        }
                    }, recyclerScope
                )
            }
        }

        override fun unBind() {

        }
    }

}