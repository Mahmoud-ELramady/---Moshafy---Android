package com.elramady.moshafy.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.ReciationsAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.ConnectivityUtil
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.RecitionsViewModel
import com.elramady.moshafy.databinding.ActivityReciationsBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class ReciationsActivity : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog
    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: RecitionsViewModel
    val adapterReciation: ReciationsAdapter = ReciationsAdapter(this,this)
    lateinit var db: DataBase
    private lateinit var roomViewModel: RoomViewModel
//    val play=PlayScreenFragment()

     var id:String=""
    companion object{
        var name_reciter:String=""
        var shuffleBoolean:Boolean=false
        var repeatBoolean:Boolean=false

        var MUSIC_LAST_PLAYED:String="LAST_PLAYED"
        var MUSIC_FILE:String="STORED_MUSIC"
        var SHOW_MINI_PLAYER:Boolean=false

        var SURAH_TO_FRAG:String?=null
        var RECITER_TO_FRAG:String?=null

        var RECITER_NAME:String="RECITER NAME"
        var SURAH_NAME:String="SURAH NAME"
    }

    lateinit var rc: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityReciationsBinding = DataBindingUtil.setContentView(this,R.layout.activity_reciations)
        loadingDialog= LoadingDialog(this)

         name_reciter=intent.getStringExtra("name_reciter").toString()

        id= intent.getStringExtra("id_reciter").toString()


        val toolbar=binding.toolbarReciations
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title=name_reciter


        apiService= SwarClient.getReciationsClient()

        repo= QuranRepositary(apiService)
        viewModel=getViewModel(id)

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        rc =binding.reciationsList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapterReciation
        rc.setHasFixedSize(true)



    }

    override fun onStart() {
        super.onStart()
        checkRun()
    }



     fun checkRun() {

        roomViewModel.isRowReciationExist(id)


        roomViewModel.checkReciationInDb.observe(this, Observer {
            if (it){


                 roomViewModel.getReciationsNames(id)
                 roomViewModel.reciationsNames.observe(this, Observer {
                     adapterReciation.setList(it.surasData)
                 })



            }else{
                if (ConnectivityUtil.isConnected(this)){
                    loadingDialog.startLoadingDialog()
                    if (ConnectivityUtil.isConnectedFast(this)){
                        viewModel.Recitions.observe(this, Observer {
                            roomViewModel.insertReciatinsNames(it)
                            adapterReciation.setList(it.surasData)
                            loadingDialog.dismissDialog()
                        })
                    }else{
                        Toast.makeText(this,"اتصال الأنترنت لديك بطئ جدا", Toast.LENGTH_SHORT).show()
                        finish()
                    }


                }else{
                    Toast.makeText(this,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        })

    }


    fun getViewModel(id:String):RecitionsViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RecitionsViewModel::class.java)){
                    return RecitionsViewModel(repo,id) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[RecitionsViewModel::class.java]


    }


    fun getRoomViewModel(): RoomViewModel {
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)){
                    return RoomViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[RoomViewModel::class.java]


    }


    override fun onResume() {
        super.onResume()
        val preferences:SharedPreferences=getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
        val value:String=preferences.getString(MUSIC_FILE,null).toString()

        val nameReciter:String=preferences.getString(RECITER_NAME,null).toString()
        val nameSurah:String=preferences.getString(SURAH_NAME,null).toString()


        if (nameReciter!=null ||nameSurah!=null){
            SHOW_MINI_PLAYER=true
            SURAH_TO_FRAG=nameSurah
            RECITER_TO_FRAG= nameReciter
        }else{
            SHOW_MINI_PLAYER=false
            SURAH_TO_FRAG=null
            RECITER_TO_FRAG= null
        }
    }
}