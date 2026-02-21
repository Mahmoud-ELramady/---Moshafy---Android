package com.elramady.moshafy.mushaf.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.mushaf.data.local.entity.MushafBookmark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MushafBookmarksAdapter(
    private val onBookmarkClick: (MushafBookmark) -> Unit
) : ListAdapter<MushafBookmark, MushafBookmarksAdapter.ViewHolder>(BookmarkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view, onBookmarkClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onBookmarkClick: (MushafBookmark) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val text1: TextView = itemView.findViewById(android.R.id.text1)
        private val text2: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(bookmark: MushafBookmark) {
            text1.text = itemView.context.getString(R.string.page_indicator_format, bookmark.pageNumber, 604)
            text2.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(bookmark.timestamp))
            itemView.setOnClickListener { onBookmarkClick(bookmark) }
        }
    }

    private class BookmarkDiffCallback : DiffUtil.ItemCallback<MushafBookmark>() {
        override fun areItemsTheSame(oldItem: MushafBookmark, newItem: MushafBookmark) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MushafBookmark, newItem: MushafBookmark) =
            oldItem == newItem
    }
}
