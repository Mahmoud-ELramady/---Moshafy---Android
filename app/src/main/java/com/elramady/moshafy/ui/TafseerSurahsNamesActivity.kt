package com.elramady.moshafy.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.TafseerSurahsNamesAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.SurhasNamesViewModel
import com.elramady.moshafy.databinding.ActivityTafseerSurahsNamesBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class TafseerSurahsNamesActivity : AppCompatActivity() {

    lateinit var loadingDialog: LoadingDialog

    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: SurhasNamesViewModel
    val adapter: TafseerSurahsNamesAdapter = TafseerSurahsNamesAdapter(this)
    lateinit var db: DataBase
    private lateinit var roomViewModel: RoomViewModel

    companion object{
        var id_tafseer=0
        lateinit var name_tafseer:String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTafseerSurahsNamesBinding = DataBindingUtil.setContentView(this,R.layout.activity_tafseer_surahs_names)

        id_tafseer=intent.getIntExtra("id_tafseer",0)
        name_tafseer=intent.getStringExtra("name_tafseer").toString()

        binding.txtTitleTafseerSurahsNames.text= name_tafseer

        loadingDialog= LoadingDialog(this)

        apiService= SwarClient.getSwarClient()

        repo= QuranRepositary(apiService)

        viewModel=getViewModel()

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        val rc: RecyclerView =binding.tafseerSurahsNamesList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapter
        rc.setHasFixedSize(true)


        checkRun()

    }












    @SuppressLint("CheckResult")
    fun checkRun(){

        var pref: SharedPreferences =getSharedPreferences("PTafseersurahs", MODE_PRIVATE)

        if(pref.getBoolean("firstTafseersurahs",true)){
            loadingDialog.startLoadingDialog()
            val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
            if (networkInfo!=null && networkInfo.isConnected()){
                viewModel.SurhasNamesList.observe(this, Observer {
                    roomViewModel.insertSurahsNamesList(it)
                    adapter.setList(it)
                    pref.edit().putBoolean("firstTafseersurahs", false).commit()
                    loadingDialog.dismissDialog()
                })

            }else{

                Toast.makeText(this,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                finish()
            }

        }else{

            roomViewModel.getSurahsNamesList()
            roomViewModel.surahsNamesListDb.observe(this, Observer {
                adapter.setList(it)
            })



        }
    }










    @JvmName("getViewModel1")
    fun getViewModel():SurhasNamesViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SurhasNamesViewModel::class.java)){
                    return SurhasNamesViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[SurhasNamesViewModel::class.java]


    }



    fun getRoomViewModel():RoomViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)){
                    return RoomViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[RoomViewModel::class.java]


    }





}