package com.sakal.playlistmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.data.Track
import com.sakal.viewHolders.SearchViewHolder

class SearchRecyclerAdapter(
    private val items: ArrayList<Track>,
    private val context: Context
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_recycler, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(items[position])

    }
}