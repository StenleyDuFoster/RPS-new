package com.stenleone.rockpaperscissors.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.databinding.ItemGameLayBinding
import com.stenleone.rockpaperscissors.interfaces.RecyclerHolder
import com.stenleone.rockpaperscissors.interfaces.SimpleClickListener
import com.stenleone.rockpaperscissors.model.network.GameUser
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import javax.inject.Inject

class GamersAdapter @Inject constructor() : BaseRecycler<GameUser>() {

    lateinit var clickListener: SimpleClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameLay(ItemGameLayBinding.inflate(LayoutInflater.from(parent.context)))
    }

    inner class GameLay(val binding: ItemGameLayBinding) : RecyclerView.ViewHolder(binding.root), RecyclerHolder {

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

}