package com.stenleone.rockpaperscissors.ui.adapters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.databinding.ItemGameFindBinding
import com.stenleone.rockpaperscissors.interfaces.RecyclerHolder
import com.stenleone.rockpaperscissors.interfaces.SimpleClickListener
import com.stenleone.rockpaperscissors.model.network.Room
import com.stenleone.rockpaperscissors.ui.adapters.recycler.base.BaseRecycler
import com.stenleone.stanleysfilm.util.extencial.throttleClicks
import javax.inject.Inject

class GameFindAdapter @Inject constructor() : BaseRecycler<Room>() {

    lateinit var clickListener: SimpleClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GameLay(ItemGameFindBinding.inflate(LayoutInflater.from(parent.context)))
    }

    inner class GameLay(val binding: ItemGameFindBinding) : RecyclerView.ViewHolder(binding.root), RecyclerHolder {

        override fun bind() {
            binding.apply {

                room = listItems[adapterPosition]
                binding.root.throttleClicks(
                    {
                        if (this@GameFindAdapter::clickListener.isInitialized) {
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