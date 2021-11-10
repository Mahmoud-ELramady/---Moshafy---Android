package com.elramady.moshafy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.TafseerNamesItemBinding
import com.elramady.moshafy.ui.TafseerSurahsNamesActivity
import com.elramady.moshafy.vo.TafseersNames.TafseersNamesItem

class TafseersNamesAdapter(val context: Context): RecyclerView.Adapter<TafseersNamesAdapter.TafseersNamesViewHolder>() {

    var tafseersNamesList= ArrayList<TafseersNamesItem>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TafseersNamesAdapter.TafseersNamesViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : TafseerNamesItemBinding=
            DataBindingUtil.inflate(layoutInflater, R.layout.tafseer_names_item,parent,false)
        return TafseersNamesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TafseersNamesAdapter.TafseersNamesViewHolder, position: Int) {
        val tafseersNames: TafseersNamesItem =tafseersNamesList[position]
        holder.bind(tafseersNames,context)
    }

    override fun getItemCount(): Int {
            return tafseersNamesList.size
    }


    fun setList(tafseersNamesList: List<TafseersNamesItem>) {
        this.tafseersNamesList= tafseersNamesList as ArrayList<TafseersNamesItem>
        notifyDataSetChanged()

    }


   inner class TafseersNamesViewHolder(val binding: TafseerNamesItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(tafseersNames: TafseersNamesItem, context: Context) {
            binding.nameTafseer.text=tafseersNames.name
            binding.nameAuthorTafseer.text=tafseersNames.author
            binding.containerTafseerNames.setOnClickListener {

                val intent= Intent(context, TafseerSurahsNamesActivity::class.java)
                intent.putExtra("id_tafseer",tafseersNames.id)
                intent.putExtra("name_tafseer",tafseersNames.name)
                context.startActivity(intent)
            }

        }

    }



}