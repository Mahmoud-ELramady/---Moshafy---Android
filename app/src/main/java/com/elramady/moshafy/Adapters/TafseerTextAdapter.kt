package com.elramady.moshafy.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.TafseerTextItemBinding
import com.elramady.moshafy.vo.SurahText.Verse

class TafseerTextAdapter (val context: Context,val  listner:ClickListner): RecyclerView.Adapter<TafseerTextAdapter.TafseerTextViewHolder>() {

    var tafseerAyatList= ArrayList<Verse>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TafseerTextAdapter.TafseerTextViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : TafseerTextItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.tafseer_text_item,parent,false)
        return TafseerTextViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TafseerTextAdapter.TafseerTextViewHolder, position: Int) {
        val tafseerAya: Verse =tafseerAyatList[position]
        holder.bind(tafseerAya,context)
    }

    override fun getItemCount(): Int {
        return tafseerAyatList.size
    }

    fun setList(tafseerTextList: List<Verse>) {
        this.tafseerAyatList= tafseerTextList as ArrayList<Verse>
        notifyDataSetChanged()

    }

   inner class TafseerTextViewHolder(val binding: TafseerTextItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tafseerAya: Verse, context: Context){
//            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerSurahsNames)
            binding.textTafseerItem.text=tafseerAya.text
            binding.tafseerNumberAya.text=tafseerAya.number.toString()

            binding.containerTafseerText.setOnClickListener {
                    val position=position
                Log.e("positionAdapter",(position+1).toString())
//                Toast.makeText(context,position,Toast.LENGTH_SHORT).show()
                    if (position!=RecyclerView.NO_POSITION){
                        listner.onItemClick(position)
                    }

            }

        }





    }


    interface ClickListner{
        fun onItemClick(position:Int)
    }

}