package com.elramady.moshafy.ui.ui.Fragments

import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elramady.moshafy.Adapters.AzkarListeningAdapter
import com.elramady.moshafy.Api.SwarClient
import com.elramady.moshafy.Api.SwarInterface
import com.elramady.moshafy.ConnectivityUtil
import com.elramady.moshafy.FragmentPlayer.PlayerReciationActivity
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.room.DataBase
import com.elramady.moshafy.room.RoomViewModel
import com.elramady.moshafy.ViewModel.AzkarListenViewModel
import com.elramady.moshafy.databinding.FragmentAzkarListeningBinding
import com.elramady.moshafy.repo.QuranRepositary
import java.lang.IllegalArgumentException


class AzkarListeningFragment : Fragment(),AzkarListeningAdapter.ClickListner {

lateinit var binding:FragmentAzkarListeningBinding
    lateinit var loadingDialog: LoadingDialog

    lateinit var repo: QuranRepositary
    lateinit var apiService: SwarInterface
    lateinit var viewModel: AzkarListenViewModel
    lateinit var db: DataBase
    private lateinit var roomViewModel: RoomViewModel
    lateinit var adapter:AzkarListeningAdapter

    companion object{
        val pathName="Azkar_Qurany.mp3"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_azkar_listening, container, false)
        loadingDialog= LoadingDialog(activity!!)

        apiService= SwarClient.getAzkarClient()

        repo= QuranRepositary(apiService)

        viewModel=getViewModel()

        db= DataBase.getInstance(context!!)
        roomViewModel=getRoomViewModel()

        val rc: RecyclerView =binding.azkarListListening
        rc.layoutManager= LinearLayoutManager(context)
       adapter = AzkarListeningAdapter(activity!!,this)

        rc.adapter=adapter
        rc.setHasFixedSize(true)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkRun()
    }



    fun checkRun(){

        var pref: SharedPreferences =activity!!.getSharedPreferences("getPrefsAzkar", AppCompatActivity.MODE_PRIVATE)

        if(pref.getBoolean("firstRunAzkar",true)){
            loadingDialog.startLoadingDialog()
            val manager: ConnectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? =manager.getActiveNetworkInfo()
            if (networkInfo!=null && networkInfo.isConnected()){
                viewModel.azkar.observe(this, Observer {
                    roomViewModel.insertAzkarListeningList(it)
                    adapter.setList(it)
                    pref.edit().putBoolean("firstRunAzkar", false).commit()
                    loadingDialog.dismissDialog()
                })

            }else{

                Toast.makeText(context,"تأكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show()
                binding.errorText.visibility=View.VISIBLE
            }

        }else{

            roomViewModel.getazkarListeningList()
            roomViewModel.azkarListeningListDb.observe(this, Observer {
                adapter.setList(it)
            })



        }
    }



    @JvmName("getViewModel1")
    fun getViewModel():AzkarListenViewModel{
        return ViewModelProvider(this,object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AzkarListenViewModel::class.java)){
                    return AzkarListenViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown View Model Class")

            }
        })[AzkarListenViewModel::class.java]


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






    private fun alertDialog(url:String,name: String,reader: String) {
        val alertDialog= AlertDialog.Builder(context!!)
        alertDialog.setTitle("تحميل الأذكار")
        alertDialog.setIcon(R.drawable.icon_logo)
        alertDialog.setMessage("تأكيد تحميل الأذكار؟")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("نعم",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (setPermission()==true){
                    downloadReciation(url,name,reader,pathName)

                }else{
                    setPermission()
                }
            }
        })

        alertDialog.setNegativeButton("لا",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                Toast.makeText(context,"حسنا!",Toast.LENGTH_SHORT).show()
            }
        })
        val alertDialog2=alertDialog.create()
        alertDialog2.show()
    }




    fun setPermission():Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PlayerReciationActivity.REQUESTED_CODE
                )

                return false
            }
        }

        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PlayerReciationActivity.REQUESTED_CODE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(context, "Permission is allowed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Permission is denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    fun downloadReciation(url:String,name: String,reader: String,outPutFileName:String) {
        if (ConnectivityUtil.isConnected(context!!)){
            Toast.makeText(context,"انظر لوحة الاشعارات",Toast.LENGTH_SHORT).show()
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(name+" - "+reader)
            request.setDescription("قرآنى")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,outPutFileName)
            val manager=activity!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
        }else{
            Toast.makeText(context,"تأكد من اتصالك بالأنترنت", Toast.LENGTH_SHORT).show()
            activity!!.finish()
        }


    }

    override fun onItemClick(position: Int, url: String, name: String, reader: String) {
        alertDialog(url,name,reader)
    }


}