package com.stenleone.rockpaperscissors.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
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
        return size
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