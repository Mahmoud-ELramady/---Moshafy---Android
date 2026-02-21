package com.elramady.moshafy.mushaf.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.mushaf.config.MushafConfig
import com.github.chrisbanes.photoview.PhotoView

class MushafPageAdapter(
    private val pageLoader: MushafPageLoader
) : RecyclerView.Adapter<MushafPageAdapter.PageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mushaf_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val pageNumber = position + 1
        holder.bind(pageNumber)
    }

    override fun getItemCount(): Int = MushafConfig.TOTAL_PAGES

    inner class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photoView: PhotoView = itemView.findViewById(R.id.photo_view)

        fun bind(pageNumber: Int) {
            pageLoader.loadPage(pageNumber, photoView)
        }
    }
}
