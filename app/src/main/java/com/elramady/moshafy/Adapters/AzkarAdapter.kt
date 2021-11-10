package com.elramady.moshafy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.AzkarItemsBinding

class AzkarAdapter (val context: Context): RecyclerView.Adapter<AzkarAdapter.AzkarViewHolder>() {
    var array= ArrayList<String>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AzkarViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : AzkarItemsBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.azkar_items,parent,false)
        return AzkarViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AzkarViewHolder, position: Int) {
        val data: String =array[position]
        holder.bind(data,context)
    }


    override fun getItemCount(): Int {
        return array.size
    }

    fun setList(array: ArrayList<String>) {
        this.array= array
        notifyDataSetChanged()

    }



    class AzkarViewHolder(val binding: AzkarItemsBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: String, context: Context){
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerAzkar)
            binding.titleAzkarTx.text=data






        }

    }
}