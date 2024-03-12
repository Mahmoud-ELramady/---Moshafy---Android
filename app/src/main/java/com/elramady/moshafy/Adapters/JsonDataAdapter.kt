package com.elramady.moshafy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.elramady.moshafy.FragmentPlayer.PlayerReciationActivity
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ItemJsonBinding
import com.elramady.moshafy.databinding.SurhasNamesItemBinding
import com.elramady.moshafy.ui.DetailsJsonActivity
import com.elramady.moshafy.ui.TextSurahActivity
import com.elramady.moshafy.vo.SurahsNames.Data
import com.elramady.moshafy.vo.jsonModel.JsonModelItem

class JsonDataAdapter(val context:Context): RecyclerView.Adapter<JsonDataAdapter.JsonDataViewHolder>() {
    var jsonModelList= ArrayList<JsonModelItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JsonDataViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding : ItemJsonBinding=
            DataBindingUtil.inflate(layoutInflater,R.layout.item_json,parent,false)
        return JsonDataViewHolder(binding)

    }

    override fun onBindViewHolder(holder: JsonDataViewHolder, position: Int) {
        val data: JsonModelItem =jsonModelList[position]
        holder.bind(data,context)
    }


    override fun getItemCount(): Int {
        return jsonModelList.size
    }

    fun setList(jsonList: List<JsonModelItem>) {
        this.jsonModelList= jsonList as ArrayList<JsonModelItem>
        notifyDataSetChanged()

    }



    class JsonDataViewHolder(val binding: ItemJsonBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: JsonModelItem, context: Context){
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerTitleJson)

            binding.titleJson.text=data.number


            binding.containerTitleJson.setOnClickListener {
                val intent: Intent = Intent(context, DetailsJsonActivity::class.java)
                intent.putExtra("titleJson", data.number)
                intent.putExtra("descJson", data.label)
                context.startActivity(intent)
            }

//


        }

    }




}