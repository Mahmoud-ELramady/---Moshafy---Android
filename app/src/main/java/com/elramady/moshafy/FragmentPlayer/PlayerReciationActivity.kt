package com.elramady.moshafy.FragmentPlayer

import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.elramady.moshafy.Adapters.ReciationsAdapter
import com.elramady.moshafy.ConnectivityUtil
import com.elramady.moshafy.LoadingDialog
import com.elramady.moshafy.R
import com.elramady.moshafy.databinding.ActivityPlayerReciationBinding
import com.elramady.moshafy.ui.ReciationsActivity
import com.elramady.moshafy.vo.RecitersDetails.SurasData
import java.util.*
import kotlin.collections.ArrayList

class PlayerReciationActivity : AppCompatActivity(),ActionPlaying,Runnable,ServiceConnection {

   lateinit var binding: ActivityPlayerReciationBinding

    var url: String = ""
    var nameReciter: String = ""
    var nameSurah: String = ""
    var position: Int = 0

    var reciationsFrag = ArrayList<SurasData>()

    lateinit var loadingDialog: LoadingDialog
    var handler: Handler = Handler()

    lateinit var playThread: Thread
    lateinit var prevThread: Thread
    lateinit var nextThread: Thread
    lateinit var closeThread: Thread
    lateinit var playFirstThread: Thread
     var musicService: MusicService?=null
    lateinit var fragPlayNow:PlayingNowFragment

lateinit var pref:SharedPreferences
    companion object{
        val pathName="Qurany.mp3"
        val REQUESTED_CODE:Int=1
        var isDestroy=false

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,R.layout.activity_player_reciation)

        loadingDialog= LoadingDialog(this)




        fragPlayNow= PlayingNowFragment()
        fragPlayNow.setActionFrag(this)

        url = intent.getStringExtra("url")!!
        nameSurah = intent.getStringExtra("surah_Name")!!
        nameReciter = ReciationsActivity.name_reciter
        position = intent.getIntExtra("position", 0)


        binding.reciterReciationNameTextView.isSelected=true

        getIntentMethod()


        binding.reciterReciationNameTextView.text =
            "$nameReciter - $nameSurah           $nameReciter - $nameSurah"

        binding.downloadButton.setOnClickListener {
            alertDialog()
        }

        binding.seekPlayer.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (musicService != null && fromUser) {

                    musicService!!.seekTo(progress * 1000)
                }

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }


        })
        runOnUiThread(Runnable {
            run()
        })






        binding.shuffleImageView.setOnClickListener {
            if (ReciationsActivity.shuffleBoolean) {
                ReciationsActivity.shuffleBoolean = false
                binding.shuffleImageView.setImageResource(R.drawable.ic_baseline_shuffle_24)
            } else {
                ReciationsActivity.shuffleBoolean = true
                binding.shuffleImageView.setImageResource(R.drawable.shuffle_on)
            }
        }



        binding.repeatImageView.setOnClickListener {
            if (ReciationsActivity.repeatBoolean) {
                ReciationsActivity.repeatBoolean = false
                binding.repeatImageView.setImageResource(R.drawable.ic_repeat)
            } else {
                ReciationsActivity.repeatBoolean = true
                binding.repeatImageView.setImageResource(R.drawable.repeat_on)
            }
        }

    }



    private fun alertDialog() {
        val alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("تحميل التلاوة")
        alertDialog.setIcon(R.drawable.icon_logo)
        alertDialog.setMessage("تأكيد تحميل التلاوة؟")
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("نعم",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if (setPermission()==true){
                    downloadReciation(url, pathName)
                }else{
                    setPermission()
                }
            }
        })

       alertDialog.setNegativeButton("لا",object :DialogInterface.OnClickListener{
           override fun onClick(dialog: DialogInterface?, which: Int) {
               Toast.makeText(this@PlayerReciationActivity,"حسنا!",Toast.LENGTH_SHORT).show()
           }
       })
        val alertDialog2=alertDialog.create()
        alertDialog2.show()
    }



    fun setPermission():Boolean{
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUESTED_CODE
                )

                return false
            }
        }


        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUESTED_CODE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission is allowed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission is denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    fun downloadReciation(url:String,outPutFileName:String) {
        if (ConnectivityUtil.isConnected(this)){
            Toast.makeText(this@PlayerReciationActivity,"انظر لوحة الاشعارات",Toast.LENGTH_SHORT).show()
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            request.setTitle(nameSurah+" - "+nameReciter)
            request.setDescription("قرآنى")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.allowScanningByMediaScanner()
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,outPutFileName)
            val manager=getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)

        }else{
            Toast.makeText(this,"تأكد من اتصالك بالأنترنت", Toast.LENGTH_SHORT).show()
            finish()
        }


    }


    fun getIntentMethod() {
        reciationsFrag = ReciationsAdapter.reciationsList
        binding.playPauseLayout.setImageResource(R.drawable.pause_audio)

        val action="PLAY"
        val intent: Intent = Intent(this,MusicService::class.java)
        intent.putExtra("urlService", url)
        intent.putExtra("nameReciterService", nameReciter)
        intent.putExtra("nameSurahService", nameSurah)
        intent.putExtra("positionSevice",position)
        intent.action=action
        startService(intent)
    }

    private fun metaData() {
        this.runOnUiThread(Runnable {


            val durationTotal = (musicService!!.getDuration().toInt() / 1000)
            Log.e("printmeddia",(musicService!!.getDuration()) .toString())

            binding.timeDurationPlayer.text = formattedTime(durationTotal)
            Log.e("durationD", durationTotal.toString())
        })
    }

    private fun formattedTime(mCurrentPosition: Int): String {
        var totalOut=""
        var totalNew=""
        val minutes= (mCurrentPosition/60).toString()
        val seconds=(mCurrentPosition%60).toString()
        totalOut=minutes + ":" + seconds
        totalNew=minutes + ":" +"0"+ seconds
        if (seconds.length==1){
            return totalNew
        }else{
            return totalOut
        }

    }



    override fun run() {
        this.runOnUiThread(Runnable {
            if (musicService != null) {
                val mCurrentPosition = (musicService!!.getCurrentPosition().toInt() ) / 1000
                binding.seekPlayer.progress=mCurrentPosition
                binding.timePlayer.text = formattedTime(mCurrentPosition)
            }
            handler.postDelayed(this, 1000)
        })



    }







    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val myBinder: MusicService.MyBinder = service as MusicService.MyBinder
        musicService = myBinder.getService()
        musicService!!.callBack(this)


        binding.seekPlayer.max = (musicService!!.getDuration()) / 1000
        metaData()



        musicService!!.showNotification(R.drawable.pause_noti,nameReciter,nameSurah)
        musicService!!.onCompleted()





    }

    override fun onServiceDisconnected(name: ComponentName?) {
//musicService=null
    }

    override fun onPause() {
        super.onPause()
       unbindService(this)

    }



    override fun onResume() {

        var intent:Intent=Intent(this,MusicService::class.java)
        bindService(intent,this, Context.BIND_AUTO_CREATE)
        playThreadBtn()
        prevThreadBtn()
        nextThreadBtn()
      //  closeBtnClick()
        super.onResume()

    }



    private fun playThreadBtn() {
        playThread=Thread(Runnable {
            binding.playPauseLayout.setOnClickListener {
                playPauseBtnClick()
            }
        })
        playThread.start()

    }

    override fun onStart() {
        super.onStart()
        pref=getSharedPreferences("isPlayingDestroy", MODE_PRIVATE)
        pref.edit().putBoolean("isPlayingDestroy", false).apply()
    }
    override fun onDestroy() {
        pref.edit().putBoolean("isPlayingDestroy", true).apply()

        super.onDestroy()
        Log.e("destroyMusic","destroyMusic")
    }


    override fun playPauseBtnClick() {
        if (musicService!!.isPlaying()){

            binding.playPauseLayout.setImageResource(R.drawable.play_audio)
        musicService!!.showNotification(R.drawable.play_noti,nameReciter,nameSurah)


            musicService!!.pause()
            binding.seekPlayer.max=musicService!!.getDuration()/1000

            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    val mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.progress=mCurrentPosition
                }
                handler.postDelayed(this,1000)

            })


        }else{


        musicService!!.showNotification(R.drawable.pause_noti,nameReciter,nameSurah)
            binding.playPauseLayout.setImageResource(R.drawable.pause_audio)

            musicService!!.start()
            binding.seekPlayer.max=musicService!!.getDuration()/1000

            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    var mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.progress=mCurrentPosition
                }
                handler.postDelayed(this,1000)

            })


        }

    }



    private fun prevThreadBtn() {
        prevThread=Thread(Runnable {
            binding.prevImageView.setOnClickListener {
                prevBtnClick()
            }
        })
        prevThread.start()

    }



    override fun prevBtnClick() {
//        loadingDialog.startLoadingDialog()
        reciationsFrag=ReciationsAdapter.reciationsList
        if (musicService!!.isPlaying()){
            musicService!!.stop()
            musicService!!.release()

            if (ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=getPosition(reciationsFrag.size - 1)
            }else if (!ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                if (position-1<0){
                    position= reciationsFrag.size-1
                }else{
                    position=position-1
                }
            }

            url= reciationsFrag.get(position).url
            Log.e("urlPrev",url)
            musicService!!.createMediaPlayer(url)
            metaData()
            nameSurah= reciationsFrag.get(position).name
            binding.reciterReciationNameTextView.text= nameReciter+" - "+nameSurah+"           "+nameReciter + " - " + nameSurah
            binding.seekPlayer.max=musicService!!.getDuration()/1000

            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    val mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.setProgress(mCurrentPosition)
                }
                handler.postDelayed(this,1000)

            })
            musicService!!.onCompleted()
        musicService!!.showNotification(R.drawable.pause_noti,nameReciter,nameSurah)
            binding.playPauseLayout.setImageResource(R.drawable.pause_audio)
            musicService!!.start()

        }else{
            musicService!!.stop()
            musicService!!.release()
            if (ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=getPosition(reciationsFrag.size - 1)
            }else if (!ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                if (position-1<0){
                    position= reciationsFrag.size-1
                }else{
                    position=position-1
                }
            }
            url= reciationsFrag.get(position).url

            musicService!!.createMediaPlayer(url)
            metaData()
            nameSurah= reciationsFrag.get(position).name
            binding.reciterReciationNameTextView.text= nameReciter+" - "+nameSurah+"           "+nameReciter + " - " + nameSurah
            binding.seekPlayer.max=musicService!!.getDuration()/1000


            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    var  mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.setProgress(mCurrentPosition)
                }
                handler.postDelayed(this,1000)

            })
            musicService!!.onCompleted()


        musicService!!.showNotification(R.drawable.play_noti,nameReciter,nameSurah)
            binding.playPauseLayout.setImageResource(R.drawable.play_audio)
        }

//        loadingDialog.dismissDialog()
    }

    override fun closeBtnClick() {
        musicService!!.stop()
        binding.playPauseLayout.setImageResource(R.drawable.play_audio)

        //   musicService!!.showNotification(R.drawable.play_noti,nameReciter,nameSurah)
//        closeThread=Thread(Runnable {
//            if (musicService!!.isPlaying()){
//
//            }else{
//
//            }
//
//        })

    }


    public fun nextThreadBtn() {
        nextThread=Thread(Runnable {
            binding.nextImageView.setOnClickListener {
                nextBtnClick()
            }
        })
        nextThread.start()


    }
    public override fun nextBtnClick()  {
        if (musicService!!.isPlaying()){
            musicService!!.stop()
            musicService!!.release()

            if (ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=getPosition(reciationsFrag.size - 1)
            }else if (!ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=(position+1) % reciationsFrag.size
            }

            url= reciationsFrag.get(position).url
            musicService!!.createMediaPlayer(url)
            metaData()

            nameSurah= reciationsFrag.get(position).name
            binding.reciterReciationNameTextView.text= nameReciter+" - "+nameSurah+"           "+nameReciter + " - " + nameSurah
            binding.seekPlayer.max= musicService!!.getDuration()/1000

            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    var  mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.setProgress(mCurrentPosition)
                }
                handler.postDelayed(this,1000)

            })


            musicService!!.onCompleted()
        musicService!!.showNotification(R.drawable.pause_noti,nameReciter,nameSurah)
        binding.playPauseLayout.setBackgroundResource(R.drawable.pause_audio)
            musicService!!.start()


        }else{
            musicService!!.stop()
            musicService!!.release()

            if (ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=getPosition(reciationsFrag.size - 1)
            }else if (!ReciationsActivity.shuffleBoolean && !ReciationsActivity.repeatBoolean){
                position=(position+1) % reciationsFrag.size
            }
            url= reciationsFrag.get(position).url
            musicService!!.createMediaPlayer(url)
            metaData()
            nameSurah= reciationsFrag.get(position).name

            binding.reciterReciationNameTextView.text= nameReciter+" - "+nameSurah+"           "+nameReciter + " - " + nameSurah
            binding.seekPlayer.max=musicService!!.getDuration()/1000


            this.runOnUiThread(Runnable {
                if (musicService!=null){
                    var mCurrentPosition=musicService!!.getCurrentPosition()/1000
                    binding.seekPlayer.setProgress(mCurrentPosition)
                }
                handler.postDelayed(this,1000)

            })
            musicService!!.onCompleted()
            musicService!!.showNotification(R.drawable.play_noti,nameReciter,nameSurah)
            binding.playPauseLayout.setBackgroundResource(R.drawable.play_audio)



        }
    }



    private fun getPosition(i: Int): Int {
        val random: Random = Random()
        return random.nextInt(i+1)
    }





}