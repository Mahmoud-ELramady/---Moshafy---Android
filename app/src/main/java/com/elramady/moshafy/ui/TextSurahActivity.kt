package com.elramady.moshafy.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.SurahsTextAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.TextArabicViewModel
import com.elramady.moshafy.databinding.ActivityTextSurahBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class TextSurahActivity : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog

    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: TextArabicViewModel
    val adapterText: SurahsTextAdapter = SurahsTextAdapter(this)
    lateinit var db: DataBase
    private lateinit var roomViewModel:RoomViewModel
    var idSurah:Int=0

    companion object{
        lateinit var name_english:String
        lateinit var name_arabic:String
    }


    lateinit var rc: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityTextSurahBinding = DataBindingUtil.setContentView(this,R.layout.activity_text_surah)

        loadingDialog= LoadingDialog(this)



         name_english=intent.getStringExtra("name_english").toString()
         name_arabic=intent.getStringExtra("name_arabic").toString()
         idSurah =intent.getIntExtra("id",0)

        val toolbar=binding.toolBarTextTitle
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)




        binding.txtTitleSurahArabic.text=name_arabic
        toolbar.title=name_english


        apiService= SwarClient.getSurahTextClient()

        repo= QuranRepositary(apiService)

        viewModel=getViewModel(idSurah)

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        rc =binding.textAyatList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapterText
        rc.setHasFixedSize(true)

        checkRun()




    }






    fun checkRun(){
Log.e("enter","enter1")

        roomViewModel.isRowExist(idSurah)


            roomViewModel.checkSurahsAyatInDb.observe(this, Observer {
                if (it){
                    roomViewModel.getSurahsAyatList(idSurah)
                    roomViewModel.surahsAyatListDb.observe(this, Observer {
                        adapterText.setList(it.verses)
                    })



        }else{
            Log.e("enter","enter3")
            val manager:ConnectivityManager= getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
            if (networkInfo!=null && networkInfo.isConnected()){
                loadingDialog.startLoadingDialog()
                viewModel.SurahsTextList.observe(this, Observer {
                    roomViewModel.insertSurahsAyatList(it)
                    adapterText.setList(it.verses)
                    loadingDialog.dismissDialog()
                })

            }else{
                Toast.makeText(this,"تأكد من اتصالك بالانترنت",Toast.LENGTH_SHORT).show()
                finish()
            }

        }
     })

    }














    fun getViewModel(id:Int):TextArabicViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TextArabicViewModel::class.java)){
                    return TextArabicViewModel(repo,id) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[TextArabicViewModel::class.java]


    }


    fun getRoomViewModel(): RoomViewModel {
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RoomViewModel::class.java)){
                    return RoomViewModel(db) as T
                }
                throw IllegalArgumentException("Unknown View Model  Class")

            }
        })[RoomViewModel::class.java]


    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val id:Int = item.getItemId()
        if (item.getItemId() == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }








}