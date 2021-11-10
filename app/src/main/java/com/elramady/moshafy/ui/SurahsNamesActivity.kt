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
import com.elramady.moshafy.Adapters.SurhasNamesAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.SurhasNamesViewModel
import com.elramady.moshafy.databinding.ActivitySurahsNamesBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class SurahsNamesActivity : AppCompatActivity() {
    lateinit var loadingDialog: LoadingDialog

    lateinit var repo:QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: SurhasNamesViewModel
    val adapter: SurhasNamesAdapter = SurhasNamesAdapter(this)
    lateinit var db:DataBase

    private lateinit var roomViewModel:RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog= LoadingDialog(this)

        val binding:ActivitySurahsNamesBinding=DataBindingUtil.setContentView(this,R.layout.activity_surahs_names)
        apiService= SwarClient.getSwarClient()

        repo= QuranRepositary(apiService)

        viewModel=getViewModel()

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        val rc:RecyclerView=binding.surahNamesList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapter
        rc.setHasFixedSize(true)


checkRun()
    }

    override fun onResume() {
        super.onResume()
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu,menu)
//        val search=menu?.findItem(R.id.search_view)
//        val searchView=search?.actionView as SearchView
//        searchView.isSubmitButtonEnabled=true
////        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
////        Toast.makeText(this,"search !!",Toast.LENGTH_SHORT).show()
//
//        searchView.setOnQueryTextListener(this)
//        return true
//    }
//
//
//
//    override fun onQueryTextSubmit(query: String?): Boolean {
//        if (query!=null){
//            searchDataBase(query)
//        }
//        return true
//
//    }
//
//    override fun onQueryTextChange(query: String?): Boolean {
//        if (query!=null){
//            searchDataBase(query)
//        }
//        return true
//    }



//    @SuppressLint("CheckResult")
//    private fun searchDataBase(query:String){
//        val searchQuery="%$query"
//        val dataBase=DataBase.getInstance(this)
//
//        dataBase.surahsDao.searchNames(searchQuery)
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        {
//                            adapter.setList(it)
//                        },{
//
//                }
//                )
//
//    }

//



@SuppressLint("CheckResult")
    fun checkRun(){

    var pref:SharedPreferences=getSharedPreferences("geyPrefs", MODE_PRIVATE)

    if(pref.getBoolean("firstRun",true)){
        loadingDialog.startLoadingDialog()
        val manager:ConnectivityManager= getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
        if (networkInfo!=null && networkInfo.isConnected()){
            viewModel.SurhasNamesList.observe(this, Observer {
                roomViewModel.insertSurahsNamesList(it)
                adapter.setList(it)
                pref.edit().putBoolean("firstRun", false).commit()
                loadingDialog.dismissDialog()
            })

        }else{

            Toast.makeText(this,"تأكد من اتصالك بالانترنت",Toast.LENGTH_SHORT).show()
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