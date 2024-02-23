package com.elramady.moshafy.FragmentPlayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.CHANNEL_ID_2
import com.elramady.moshafy.FragmentPlayer.PlayerReciationActivity.Companion.isDestroy
import com.elramady.moshafy.R
import com.elramady.moshafy.vo.RecitersDetails.SurasData
import okhttp3.internal.notify


class MusicService : Service() ,MediaPlayer.OnCompletionListener {

    val mBinder:IBinder=MyBinder()
      var mediaPlayer:MediaPlayer?=null
    var reciations=ArrayList<SurasData>()
    var position:Int=0
    lateinit var sharedPreferences: SharedPreferences

    lateinit var mediaSessionCompat: MediaSessionCompat
     var notification: Notification?=null

    lateinit var actionPlaying: ActionPlaying
    lateinit var url:String

    lateinit var nameReciter: String
    lateinit var nameSurah: String
    lateinit var pref:SharedPreferences


    companion object{
        var MUSIC_LAST_PLAYED:String="LAST_PLAYED"
        var MUSIC_FILE:String="STORED_MUSIC"
        var RECITER_NAME:String="RECITER NAME"
        var SURAH_NAME:String="SURAH NAME"

    }

   inner class MyBinder: Binder() {
       fun getService():MusicService{
            return this@MusicService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("onBind","onMethod")
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        mediaSessionCompat = MediaSessionCompat(this, "My Audio")
        Log.e("closeee","new Service")
        pref=getSharedPreferences("isPlayingDestroy", MODE_PRIVATE)

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("PLAY")){
            url= intent?.getStringExtra("urlService").toString()
            nameReciter= intent?.getStringExtra("nameReciterService").toString()
            nameSurah= intent?.getStringExtra("nameSurahService").toString()
            Log.e("name",nameSurah+"   "+nameReciter)

            if (url !=null || url !=""){
                playMedia(url)
                Log.e("phase","phase")
            }
        }

        val actionName=intent?.getStringExtra("ActionName").toString()



        when (actionName){
            "playPause" -> {
                if (actionPlaying!=null){
                    actionPlaying.playPauseBtnClick()
                }
            }
            "Next" ->{
                try {

                    if (actionPlaying!=null) {
                        actionPlaying.nextBtnClick()

                    }
                }catch (e:Exception){
                    Log.e("errorNext",e.toString())
                }

            }
            "Previous"->{
                if (actionPlaying!=null){
                    actionPlaying.prevBtnClick()
                }
            }
            "Close"->{
                Log.e("closeeeeeee","closeeeeee")
            val   destroy=  pref.getBoolean("isPlayingDestroy",true)
                Log.e("closeeeeeee",destroy.toString())

                if (destroy){
                    Log.e("closeeeeeee","destroy")
                    actionPlaying.closeBtnClick()
                    stopForeground(STOP_FOREGROUND_REMOVE)

                }else{
                    Log.e("closeeeeeee","non destroy")

                    stopForeground(STOP_FOREGROUND_REMOVE)

                }

               // actionPlaying
            }

        }
        return START_STICKY
    }














    private fun playMedia(startUrl: String) {

        if (mediaPlayer!=null){
               mediaPlayer!!.stop()
                mediaPlayer!!.release()
                createMediaPlayer(startUrl)
                mediaPlayer!!.start()
        }else{
            createMediaPlayer(startUrl)
            mediaPlayer!!.start()
        }


    }




    fun start(){
        mediaPlayer!!.start()
    }
    fun pause(){
        mediaPlayer!!.pause()
    }
    fun isPlaying():Boolean{
        return mediaPlayer!!.isPlaying
    }
    fun stop(){
        mediaPlayer!!.stop()
    }
    fun release(){
        mediaPlayer!!.release()
    }
    fun getDuration():Int {
       return mediaPlayer!!.duration
    }
    fun getCurrentPosition():Int{
        return mediaPlayer!!.currentPosition

    }
    fun seekTo(position:Int){
        mediaPlayer!!.seekTo(position)
    }

    fun createMediaPlayer(urlCreate:String){
        url=urlCreate

        var editor: SharedPreferences.Editor? =getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE).edit()

        editor?.putString(RECITER_NAME,nameReciter)
        editor?.putString(SURAH_NAME,nameSurah)
        editor?.apply()

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                    AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            )
            setDataSource(urlCreate)
            prepare()
        }
    }

    fun onCompleted(){
        mediaPlayer!!.setOnCompletionListener(this)
    }

    override fun onCompletion(mp: MediaPlayer?) {

        if (actionPlaying!=null){
            actionPlaying.nextBtnClick()
            if (mediaPlayer!=null){
                createMediaPlayer(url)
                mediaPlayer!!.start()
                onCompleted()
            }
        }



    }




    @SuppressLint("ForegroundServiceType")
    fun showNotification(playPauseBtn: Int, nameReciter:String, nameSurah:String) {

        this.nameSurah=nameSurah
        this.nameReciter=nameReciter
        val intent = Intent(this, PlayerReciationActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
//        intent.addCategory(Intent.CATEGORY_LAUNCHER)
//        intent.setAction(Intent.ACTION_MAIN)


        val  notificationIntent_Close:Intent  = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_CLOSE)

       val closePending:PendingIntent  = PendingIntent.getBroadcast(this,
        0, notificationIntent_Close,
           PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE);




//        val contentIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val prevIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_PREVIOUS)
        val prevPending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val pauseIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_PLAY)
        val pausePending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val nextIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_NEXT)
        val nextPending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)



        Log.e("log2", "log2")


        val icon= BitmapFactory.decodeResource(resources,R.drawable.background_player)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_2)
                    .setSmallIcon(R.drawable.icon_logo)
                    .setContentTitle(nameSurah)
                    .setContentText(nameReciter)
                    .setLargeIcon(icon)
                    .setColor(Color.WHITE)
                .setAutoCancel(true)
              // .setContentIntent(resultPendingIntent)
                    .addAction(R.drawable.previous_audio, "Previous", prevPending)
                    .addAction(playPauseBtn, "Pause", pausePending)
                    .addAction(R.drawable.next_audio, "next", nextPending)
                     .addAction(R.drawable.ic_close,"Close",closePending)
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSessionCompat.sessionToken))
                    .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

                    .build()

            startForeground(2, notification)
        }
        }



fun callBack(actionPlaying: ActionPlaying){
    this.actionPlaying=actionPlaying
}

    fun getDismissIntent(notificationId: Int, context: Context?): PendingIntent? {
        val intent = Intent(context, ApplicationClass::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra(CHANNEL_ID_2, notificationId)
        return PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }


}