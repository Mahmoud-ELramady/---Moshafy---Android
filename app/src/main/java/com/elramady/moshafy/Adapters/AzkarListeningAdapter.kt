package com.elramady.moshafy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.AzkarListeningItemBinding
import com.elramady.moshafy.vo.AzkarListen.AzkarListeningItem

class AzkarListeningAdapter  (val context: Context,val listner: ClickListner): RecyclerView.Adapter<AzkarListeningAdapter.AzkarListeningViewHolder>() {
    var arrayAzkar= ArrayList<AzkarListeningItem>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AzkarListeningViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : AzkarListeningItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.azkar_listening_item,parent,false)
        return AzkarListeningViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AzkarListeningViewHolder, position: Int) {
        val data: AzkarListeningItem =arrayAzkar[position]
        holder.bind(data,context)
    }


    override fun getItemCount(): Int {
        return arrayAzkar.size
    }

    fun setList(arrayAzkar: List<AzkarListeningItem>) {
        this.arrayAzkar= arrayAzkar as ArrayList<AzkarListeningItem>
        notifyDataSetChanged()

    }



   inner class AzkarListeningViewHolder(val binding: AzkarListeningItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: AzkarListeningItem, context: Context){
            YoYo.with(Techniques.FadeInLeft).duration(500).playOn(binding.containerAzkarListening)
            binding.azkarKind.text=data.name
            binding.azkarNameReciter.text=data.readerName
            if (data.readerName=="محمد جبريل"){
                binding.imageAzkarReciter.setImageResource(R.drawable.mohamed_gebril)
            }else if (data.readerName=="مشاري العفاسي "||data.readerName=="مشارى العفاسى"
                 ||data.readerName=="مشارى العفاسى "){
                binding.imageAzkarReciter.setImageResource(R.drawable.mshary)
            } else if (data.readerName=="ناصر القطامي "){
                binding.imageAzkarReciter.setImageResource(R.drawable.nasr)
            }else if (data.readerName=="فارس عباد"){
                binding.imageAzkarReciter.setImageResource(R.drawable.fares_abbad)
            }else{
                binding.imageAzkarReciter.setImageResource(R.drawable.icon_logo)
            }


            binding.downloadZkarButton.setOnClickListener {
                    val position=position
                    if (position!=RecyclerView.NO_POSITION&&data.link!=null){
                        listner.onItemClick(position,data.link,data.name,data.readerName)

                    }


            }


        }


    }

    interface ClickListner{
        fun onItemClick(position:Int,url:String,name:String,reader:String)
    }
}