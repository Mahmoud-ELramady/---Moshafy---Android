package com.elramady.moshafy.ui

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.TafseerTextAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.TafseerTextViewModel
import com.elramady.moshafy.ViewModel.TextArabicViewModel
import com.elramady.moshafy.databinding.ActivityTafseerTextBinding
import com.elramady.moshafy.repo.QuranRepositary
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.IllegalArgumentException

class TafseerTextActivity : AppCompatActivity(),TafseerTextAdapter.ClickListner {
    lateinit var loadingDialog: LoadingDialog

    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: TextArabicViewModel
    lateinit var viewModelM:TafseerTextViewModel
    val adapterText:TafseerTextAdapter = TafseerTextAdapter(this,this)
    lateinit var db: DataBase
    private lateinit var roomViewModel: RoomViewModel


    var idSurah:Int=0
    var idTafseer:Int=0
    lateinit var name_surah_tafseer:String
    lateinit var name_tafseer:String



    lateinit var rc: RecyclerView
    lateinit var binding: ActivityTafseerTextBinding

    lateinit var mBottomSheetBehaviour:BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,R.layout.activity_tafseer_text)


        loadingDialog= LoadingDialog(this)

        mBottomSheetBehaviour=BottomSheetBehavior.from(binding.bottomSheet)
        mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_HIDDEN

        idSurah =intent.getIntExtra("id_surah_tafseer",0)
        name_surah_tafseer=intent.getStringExtra("name_surah_tafseer").toString()
        name_tafseer=TafseerSurahsNamesActivity.name_tafseer
        idTafseer=TafseerSurahsNamesActivity.id_tafseer


        setSupportActionBar(binding.toolBarTextTitle)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.txtTitleTafseer.text=name_tafseer
        binding.toolBarTextTitle.title=name_surah_tafseer



        apiService= SwarClient.getSurahTextClient()
        repo= QuranRepositary(apiService)
        viewModel=getViewModel(idSurah)

        db= DataBase.getInstance(this)
        roomViewModel=getRoomViewModel()

        rc =binding.textTafseerList
        rc.layoutManager= LinearLayoutManager(this)
        rc.adapter=adapterText
        rc.setHasFixedSize(true)

        checkRun()




    }

    override fun onItemClick(position: Int) {

        mBottomSheetBehaviour.state=BottomSheetBehavior.STATE_EXPANDED
        apiService= SwarClient.getTafseersClient()
        repo= QuranRepositary(apiService)
       val ayah_Number=position+1


        checkTafseerInDb(ayah_Number)



    }




    fun checkRun(){

        roomViewModel.isRowExist(idSurah)


        roomViewModel.checkSurahsAyatInDb.observe(this, Observer {
            if (it){
                roomViewModel.getSurahsAyatList(idSurah)
                roomViewModel.surahsAyatListDb.observe(this, Observer {
                    Log.e("yseee",it.toString())

                    adapterText.setList(it.verses)
                    Log.e("yseee","Yes3")

                })



            }else{
                val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
                if (networkInfo!=null && networkInfo.isConnected()){
                    loadingDialog.startLoadingDialog()
                    viewModel.SurahsTextList.observe(this, Observer {
                        roomViewModel.insertSurahsAyatList(it)
                        adapterText.setList(it.verses)
                        loadingDialog.dismissDialog()
                    })

                }else{
                    Toast.makeText(this,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                    finish()
                }

            }
        })
    getDialog()

    }


    fun checkTafseerInDb(idAya:Int){

        roomViewModel.isRowTafseerExist(idTafseer,idSurah,idAya)

        roomViewModel.checkTafsserAyaInDb.observe(this, Observer {
            if (it){
                roomViewModel.getTafseerAya(idTafseer,idSurah,idAya)
                roomViewModel.tafsserAyaDb.observe(this, Observer {
                    Log.e("it",it.toString())
                    binding.textTafseerBottom.text=it.text
                    binding.tafseerNumberAyaBottom.text=it.ayahNumber.toString()
                })
            }else{


                val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
                if (networkInfo!=null && networkInfo.isConnected()){
                    binding.pr.visibility=View.VISIBLE
                    apiService.getTafseerTextAyah(idTafseer, idSurah,idAya)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                binding.textTafseerBottom.text=it.text
                                binding.tafseerNumberAyaBottom.text=it.ayahNumber.toString()
                                binding.pr.visibility=View.GONE
                                it.surah_id=idSurah
                                roomViewModel.insertTafseerAya(it)
                                Log.e("TafseerText", "Success")

                            }, {
                                it.message?.let { it1 -> Log.e("TafseerTextfail", it1) }

                            }
                            )

                }else{
                    binding.textTafseerBottom.text="لا يوجد اتصال بالانترنت"

                }



            }
        })


    }



fun getDialog(){
    var pref: SharedPreferences =getSharedPreferences("PrefsDialogTafseer", MODE_PRIVATE)
    if(pref.getBoolean("firstDialogTafseer",true)) {
        val alertDialog= AlertDialog.Builder(this)
        alertDialog.setTitle("تنبيه!")
        alertDialog.setIcon(R.drawable.icon_logo)
        alertDialog.setMessage("لعرض التفسير اضغط على الاية المراد تفسيرها")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("حسنا, لقد فهمت !!",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
            }
        })
        val alertDialog2=alertDialog.create()
        alertDialog2.show()
        pref.edit().putBoolean("firstDialogTafseer", false).commit()

    }


}




    fun getViewModel(id:Int):TextArabicViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TextArabicViewModel::class.java)){
                    return TextArabicViewModel(repo,id) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[TextArabicViewModel::class.java]


    }

    fun getViewModelAyah(tafseerId:Int,surahNumber:Int,ayahNumber:Int):TafseerTextViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TafseerTextViewModel::class.java)){
                    return TafseerTextViewModel(repo,tafseerId,surahNumber,ayahNumber) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[TafseerTextViewModel::class.java]


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







    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id:Int = item.getItemId()
        if (item.getItemId() == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }







    /*
    Tafseer Text With View Model But not Working
              val factory= TafseerTextRequest(repo, id_tafseer, idSurah,ayah_Number)

          viewModelM=ViewModelProvider(this,factory).get(TafseerTextViewModel::class.java)

          viewModelM=getViewModelAyah(id_tafseer, idSurah,ayah_Number)
          viewModelM.TafseerTextList.observe(this, Observer {

                  binding.textTafseerBottom.text=it.text
                  binding.tafseerNumberAyaBottom.text=it.ayahNumber.toString()
                  Log.e("tafseerTextItem",it.text)
                  Log.e("tafseerTextItem","Allah")
          })

  */

}