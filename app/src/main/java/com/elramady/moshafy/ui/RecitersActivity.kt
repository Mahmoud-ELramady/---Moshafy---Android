package com.elramady.moshafy.ui

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.RecitersAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.RecitersViewModel
import com.elramady.moshafy.databinding.ActivityRecitersBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException

class RecitersActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    lateinit var loadingDialog: LoadingDialog
    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: RecitersViewModel
    lateinit var adapter: RecitersAdapter
    lateinit var db: DataBase
    private lateinit var roomViewModel: RoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRecitersBinding = DataBindingUtil.setContentView(this,R.layout.activity_reciters)

        loadingDialog= LoadingDialog(this)

        val toolbar=binding.mToolbarReciters
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        apiService= SwarClient.getRecitersClient()

        repo= QuranRepositary(apiService)

        viewModel=getRecirersViewModel()

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        val rc: RecyclerView =binding.recitersNamesList
        adapter=RecitersAdapter(this)
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapter
        rc.setHasFixedSize(true)


    }


    override fun onStart() {
        super.onStart()
        checkRun()
    }


        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        val search=menu?.findItem(R.id.search_view)
        val searchView=search?.actionView as SearchView
        searchView.isSubmitButtonEnabled=true
        searchView.queryHint="ابحث بأسم القارئ او الرواية"
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(this)
        return true
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query!=null){
            searchDataBase(query)
        }
        return true

    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query!=null){
            searchDataBase(query)
        }
        return true
    }





    private fun searchDataBase(query:String){
        val searchQuery="%$query%"
        roomViewModel.searchInReciters(searchQuery)
        roomViewModel.searchRecitersNamesList.observe(this, Observer {
                adapter.setList(it)
        })



    }





    fun checkRun(){

        var pref: SharedPreferences =getSharedPreferences("prefReciters", MODE_PRIVATE)

        if(pref.getBoolean("firstRunReciter",true)){
            loadingDialog.startLoadingDialog()
            val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
            if (networkInfo!=null && networkInfo.isConnected()){
                viewModel.recitersNamesList.observe(this, Observer {
                    roomViewModel.insertRecirersNamesList(it)
                    adapter.setList(it)
                    pref.edit().putBoolean("firstRunReciter", false).commit()
                    loadingDialog.dismissDialog()
                })

            }else{

                Toast.makeText(this,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                finish()
            }

        }else{

            roomViewModel.getRecitersNamesList()
            roomViewModel.recitersNamesListDb.observe(this, Observer {
                adapter.setList(it)
            })



        }
    }









    fun getRecirersViewModel():RecitersViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RecitersViewModel::class.java)){
                    return RecitersViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown View Modell Class")

            }
        })[RecitersViewModel::class.java]


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
//
//    override fun onItemClick(position: Int) {
////        val intent= Intent(this, MoshfPdfActivity::class.java)
////                intent.putExtra("id_reciter",position.)
//////                context.startActivity(intent)
//    }


}