package com.stenleone.rockpaperscissors.ui.adapters.recycler

import androidx.recyclerview.widget.RecyclerView
import com.stenleone.rockpaperscissors.interfaces.RecyclerHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job

abstract class BaseRecycler<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val listItems = arrayListOf<T>()

    val recyclerJob = Job()
    val recyclerScope = CoroutineScope(Main + recyclerJob)

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RecyclerHolder).bind()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as RecyclerHolder).unBind()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerJob.cancel()
        super.onDetachedFromRecyclerView(recyclerView)
    }
}