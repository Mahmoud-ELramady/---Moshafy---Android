package com.elramady.moshafy.mushaf.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.vo.SurahsNames.Data

class MushafSurahsDialogAdapter(
    private val onSurahClick: (Data) -> Unit
) : ListAdapter<Data, MushafSurahsDialogAdapter.ViewHolder>(SurahDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mushaf_surah_dialog, parent, false)
        return ViewHolder(view, onSurahClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onSurahClick: (Data) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvNumber: TextView = itemView.findViewById(R.id.tv_surah_number)
        private val tvName: TextView = itemView.findViewById(R.id.tv_surah_name)

        fun bind(surah: Data) {
            tvNumber.text = surah.number.toString()
            tvName.text = surah.name
            itemView.setOnClickListener { onSurahClick(surah) }
        }
    }

    private class SurahDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data) = oldItem.number == newItem.number
        override fun areContentsTheSame(oldItem: Data, newItem: Data) = oldItem == newItem
    }
}
