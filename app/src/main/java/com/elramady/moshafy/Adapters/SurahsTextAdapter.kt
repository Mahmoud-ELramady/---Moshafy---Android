package com.elramady.moshafy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.TextSurahItemBinding
import com.elramady.moshafy.ui.TextSurahActivity
import com.elramady.moshafy.vo.SurahText.Verse

class SurahsTextAdapter (val context: Context): RecyclerView.Adapter<SurahsTextAdapter.TextSurahViewHolder>() {


    var ayatList= ArrayList<Verse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextSurahViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding : TextSurahItemBinding =
            DataBindingUtil.inflate(layoutInflater,R.layout.text_surah_item,parent,false)
        return TextSurahViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TextSurahViewHolder, position: Int) {
        val verse: Verse =ayatList[position]
        holder.bind(verse,context)

    }

    override fun getItemCount(): Int {
        return ayatList.size
    }

    fun setList(verse: List<Verse>) {
        this.ayatList= verse as ArrayList<Verse>
        notifyDataSetChanged()

    }

    class TextSurahViewHolder(val binding: TextSurahItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(verse: Verse, context: Context){
            binding.numberAya.text=verse.number.toString()
            binding.ayaWithArabic.text=verse.text
            binding.ayaWithEnglish.text=verse.translationEn

         //   binding.ayaWithArabic.setTextAppearance(R.style.TextStyle_Quran)
         //   binding.ayaWithEnglish.setTextAppearance(R.style.TextStyle_Quran)

            binding.shareIvAya.setOnClickListener {
              val shareIntent: Intent =  Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"("+verse.text+")"+"\n\n"+"[ "+TextSurahActivity.name_arabic+" "+"("+verse.number+")"+" ]");
               context.startActivity(Intent.createChooser(shareIntent, "Share..."));
            }
        }


    }


}