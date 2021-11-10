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
import com.elramady.moshafy.databinding.RecitersNamesItemBinding
import com.elramady.moshafy.ui.ReciationsActivity
import com.elramady.moshafy.vo.RecitersNames.Reciter


class RecitersAdapter(val context:Context): RecyclerView.Adapter<RecitersAdapter.RecitersViewHolder>()  {

    var reciterList= ArrayList<Reciter>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecitersViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding : RecitersNamesItemBinding =
            DataBindingUtil.inflate(layoutInflater,R.layout.reciters_names__item,parent,false)
        return RecitersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecitersViewHolder, position: Int) {
        val reciter:Reciter =reciterList[position]
        holder.bind(reciter,context)

    }

    override fun getItemCount(): Int {
        return reciterList.size
    }

    fun setList(reciter: List<Reciter>) {
        this.reciterList= reciter as ArrayList<Reciter>
        Log.e("size",reciter.size.toString())
        notifyDataSetChanged()

    }

    inner class RecitersViewHolder(val binding:RecitersNamesItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        var playScreenFragment = PlayScreenFragment()

        fun bind(reciter: Reciter, context: Context) {
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerRecitersNames)
            binding.nameReciter.text = reciter.name
            binding.nameRwayaReciter.text = reciter.rewaya
            binding.countReciter.text = reciter.count

            binding.containerRecitersNames.setOnClickListener {
                    val intent= Intent(context, ReciationsActivity::class.java)
                    intent.putExtra("id_reciter",reciter.id)
                    intent.putExtra("name_reciter",reciter.name)
                    context.startActivity(intent)

            }


        }

    }





}


