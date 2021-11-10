package com.elramady.moshafy.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.SurhasNamesItemBinding
import com.elramady.moshafy.ui.TafseerTextActivity
import com.elramady.moshafy.vo.SurahsNames.Data

class TafseerSurahsNamesAdapter (val context: Context): RecyclerView.Adapter<TafseerSurahsNamesAdapter.TafseerSurahsNamesViewHolder>() {
    var surhasNamesList= ArrayList<Data>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TafseerSurahsNamesViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : SurhasNamesItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.surhas_names_item,parent,false)
        return TafseerSurahsNamesViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TafseerSurahsNamesViewHolder, position: Int) {
        val data: Data =surhasNamesList[position]
        holder.bind(data,context)
    }


    override fun getItemCount(): Int {
        return surhasNamesList.size
    }

    fun setList(surahsNamesList: List<Data>) {
        this.surhasNamesList= surahsNamesList as ArrayList<Data>
        notifyDataSetChanged()

    }



    class TafseerSurahsNamesViewHolder(val binding: SurhasNamesItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: Data, context: Context){
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerSurahsNames)
            binding.nameSurahArabic.text=data.name
            binding.nameSurahEnglish.text=data.englishNameTranslation
            binding.ayatNumber.text=data.numberOfAyahs.toString()
            binding.numberSurah.text=data.number.toString()
            binding.typeSurah.text=data.revelationType

            binding.containerSurahsNames.setOnClickListener {

                val intent= Intent(context, TafseerTextActivity::class.java)
                intent.putExtra("id_surah_tafseer",data.number)
                intent.putExtra("name_surah_tafseer",data.name)
                context.startActivity(intent)
                Log.e("yseee","Yes1")
            }




        }

    }




}
