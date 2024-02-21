package com.elramady.moshafy.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.TafseersNamesAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.TafseersNamesViewModel
import com.elramady.moshafy.databinding.ActivityTafseerBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class TafseerActivity : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog

    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: TafseersNamesViewModel
    val adapter: TafseersNamesAdapter = TafseersNamesAdapter(this)


    lateinit var db:DataBase
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTafseerBinding = DataBindingUtil.setContentView(this,R.layout.activity_tafseer)


        loadingDialog= LoadingDialog(this)

        apiService= SwarClient.getTafseersClient()

        repo= QuranRepositary(apiService)

        viewModel=getViewModel()

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        val rc: RecyclerView =binding.namesTafseerList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapter
        rc.setHasFixedSize(true)


        checkRun()



    }

    private fun checkRun() {


        var pref: SharedPreferences =getSharedPreferences("TafseersNamesPrefs", MODE_PRIVATE)

        if(pref.getBoolean("firstRunTafseersNames",true)){
            loadingDialog.startLoadingDialog()
            val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
            if (networkInfo!=null && networkInfo.isConnected()){
                viewModel.TafseersNamesList.observe(this, Observer {
                    Log.e("list",it.toString())
                    roomViewModel.insertTafseersNamesList(it)
                    adapter.setList(it)
                    pref.edit().putBoolean("firstRunTafseersNames", false).commit()
                    loadingDialog.dismissDialog()
                })

            }else{

                Toast.makeText(this,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                finish()
            }

        }else{
            roomViewModel.getTafseersNamesList()
            roomViewModel.tafseersNamesListDb.observe(this, Observer {
//                Log.e("list",it.toString())
                adapter.setList(it)
            })



        }
    }


    @JvmName("getViewModel1")
    fun getViewModel():TafseersNamesViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TafseersNamesViewModel::class.java)){
                    return TafseersNamesViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[TafseersNamesViewModel::class.java]


    }


    fun getRoomViewModel():RoomViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)){
                    return RoomViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[RoomViewModel::class.java]


    }


}