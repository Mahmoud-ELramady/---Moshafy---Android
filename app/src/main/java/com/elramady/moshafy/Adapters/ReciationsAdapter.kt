package com.elramady.moshafy.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.ConnectivityUtil
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ReciationsNamesItemBinding
import com.elramady.moshafy.vo.RecitersDetails.SurasData
import com.elramady.moshafy.FragmentPlayer.PlayerReciationActivity
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.ui.ReciationsActivity


class ReciationsAdapter(val context: Context,val activity: ReciationsActivity): RecyclerView.Adapter<ReciationsAdapter.ReciationsViewHolder>() {
    companion object{
     public  var reciationsList= ArrayList<SurasData>()
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReciationsViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val binding : ReciationsNamesItemBinding =
            DataBindingUtil.inflate(layoutInflater,R.layout.reciations_names_item,parent,false)
        return ReciationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReciationsViewHolder, position: Int) {
        val surasData:SurasData =reciationsList[position]
        holder.bind(surasData, context,activity)
    }

    override fun getItemCount(): Int {
        return reciationsList.size
    }

    fun setList(surasData: List<SurasData>) {
       reciationsList= surasData as ArrayList<SurasData>
        notifyDataSetChanged()

    }


   inner class ReciationsViewHolder(val binding:ReciationsNamesItemBinding) : RecyclerView.ViewHolder(binding.root){
lateinit var loadingDialog: LoadingDialog
        fun bind(surasData: SurasData, context: Context,activity: ReciationsActivity) {
            binding.nameReciations.text=surasData.name
            binding.numberReciations.text=surasData.id

            binding.containerReciationsNames.setOnClickListener {


                    loadingDialog= LoadingDialog(activity)
////                loadingDialog.startLoadingDialog()

                    if (ConnectivityUtil.isConnected(context)){
                        val intent: Intent = Intent(context, PlayerReciationActivity::class.java)
                        intent.putExtra("url", surasData.url)
                        intent.putExtra("surah_Name", surasData.name)
                        intent.putExtra("position",position)
                        context.startActivity(intent)
//                    loadingDialog.dismissDialog()
                    }else{
                        Toast.makeText(context,"تأكد من اتصالك بالأنترنت", Toast.LENGTH_SHORT).show()

                    }


                
            }

        }



    }

}
