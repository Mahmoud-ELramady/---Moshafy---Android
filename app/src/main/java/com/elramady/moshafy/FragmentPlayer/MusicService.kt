package com.elramady.moshafy.FragmentPlayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.CHANNEL_ID_2
import com.elramady.moshafy.R
import com.elramady.moshafy.vo.RecitersDetails.SurasData


class MusicService : Service() ,MediaPlayer.OnCompletionListener {

    val mBinder:IBinder=MyBinder()
      var mediaPlayer:MediaPlayer?=null
    var reciations=ArrayList<SurasData>()
    var position:Int=0
    lateinit var sharedPreferences: SharedPreferences

    lateinit var mediaSessionCompat: MediaSessionCompat
     var notification: Notification?=null

     private var actionPlaying: ActionPlaying?=null
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
        // Set MediaSessionCompat as active - required for notification to work properly
        mediaSessionCompat.isActive = true
        Log.e("closeee","new Service")
        pref=getSharedPreferences("isPlayingDestroy", MODE_PRIVATE)

    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals("PLAY")){
            val newUrl = intent?.getStringExtra("urlService").toString()
            val newNameReciter = intent?.getStringExtra("nameReciterService").toString()
            val newNameSurah = intent?.getStringExtra("nameSurahService").toString()
            Log.e("name",newNameSurah+"   "+newNameReciter)

            if (newUrl.isNotEmpty() && newUrl != "null" && newUrl != "null"){
                // Check if we're already playing the same URL - if so, just update notification
                if (mediaPlayer != null && url == newUrl && ::nameSurah.isInitialized && nameSurah == newNameSurah) {
                    // Same track already playing - just ensure notification is visible
                    val currentPos = getCurrentPosition() / 1000
                    val duration = getDuration()?.div(1000) ?: 0
                    val playPauseIcon = if (isPlaying()) R.drawable.pause_noti else R.drawable.play_noti
                    showNotification(playPauseIcon, nameReciter, nameSurah, duration, currentPos)
                } else {
                    // New track or player doesn't exist - start playback
                    url = newUrl
                    nameReciter = newNameReciter
                    nameSurah = newNameSurah
                    playMedia(url)
                    Log.e("phase","phase")
                }
            }
        }

        val actionName=intent?.getStringExtra("ActionName").toString()



        when (actionName){
            "playPause" -> {
                if (actionPlaying!=null){
                    // Activity is bound - use existing callback
                    actionPlaying?.playPauseBtnClick()
                } else if (mediaPlayer != null) {
                    // Activity not bound - handle directly in service
                    handlePlayPauseDirectly()
                }
            }
            "Next" ->{
                try {
                    if (actionPlaying!=null) {
                        // Activity is bound - use existing callback
                        actionPlaying?.nextBtnClick()
                    } else if (mediaPlayer != null) {
                        // Activity not bound - try to handle if we have reciations list
                        handleNextDirectly()
                    }
                }catch (e:Exception){
                    Log.e("errorNext",e.toString())
                }

            }
            "Previous"->{
                if (actionPlaying!=null){
                    // Activity is bound - use existing callback
                    actionPlaying?.prevBtnClick()
                } else if (mediaPlayer != null) {
                    // Activity not bound - try to handle if we have reciations list
                    handlePreviousDirectly()
                }
            }
            "Close"->{


                Log.e("closeeeeeee","closeeeeee")
                val destroy=  pref.getBoolean("isPlayingDestroy",true)
                Log.e("closeeeeeee",destroy.toString())

                if (destroy){

                    Log.e("closeeeeeee","destroy")
                    actionPlaying?.closeBtnClick()
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
    fun getDuration():Int? {
       return mediaPlayer?.duration
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
            actionPlaying?.nextBtnClick()
            if (mediaPlayer!=null){
                createMediaPlayer(url)
                mediaPlayer!!.start()
                onCompleted()
            }
        }



    }




    @SuppressLint("ForegroundServiceType")
    fun showNotification(playPauseBtn: Int, nameReciter:String, nameSurah:String,max:Int,progress:Int) {

        this.nameSurah=nameSurah
        this.nameReciter=nameReciter

        // Update MediaSessionCompat metadata and playback state
        updateMediaSessionMetadata(nameReciter, nameSurah, max)
        updateMediaSessionPlaybackState(progress, max, playPauseBtn == R.drawable.pause_noti)

        // Create intent to open PlayerReciationActivity when notification is clicked
        // This works even if app process was killed
        val intent = Intent(this, PlayerReciationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Add extras to restore state if needed
            putExtra("url", url)
            putExtra("surah_Name", nameSurah)
            putExtra("position", position)
        }
        
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack
            addNextIntentWithParentStack(intent)
            // Get the PendingIntent containing the entire back stack
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val  notificationIntent_Close:Intent  = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_CLOSE)

       val closePending:PendingIntent  = PendingIntent.getBroadcast(this,
        0, notificationIntent_Close,
           PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE);

        val prevIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_PREVIOUS)
        val prevPending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, prevIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_PLAY)
        val pausePending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, pauseIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(this, NotificationReceiver::class.java)
                .setAction(ApplicationClass.ACTION_NEXT)
        val nextPending: PendingIntent = PendingIntent
                .getBroadcast(this, 0, nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        Log.e("log2", "log2")

        val icon= BitmapFactory.decodeResource(resources,R.drawable.photo_play)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_2)
                    .setSmallIcon(R.drawable.icon_logo)
                    .setContentTitle(nameSurah)
                    .setContentText(nameReciter)
                    .setLargeIcon(icon)
                    .setColor(Color.WHITE)
                    // Add progress bar - max is duration in seconds, progress is current position in seconds
                    .setProgress(max, progress, false)
                    .setContentIntent(resultPendingIntent) // Enable notification tap to open player
                    .addAction(R.drawable.previous_audio, "Previous", prevPending)
                    .addAction(playPauseBtn, "Pause", pausePending)
                    .addAction(R.drawable.next_audio, "next", nextPending)
                     .addAction(R.drawable.ic_close,"Close",closePending)
                     .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                            .setMediaSession(mediaSessionCompat.sessionToken)
                            .setShowActionsInCompactView(0, 1, 2)) // Show prev, play/pause, next in compact view
                     .setOnlyAlertOnce(true)
                     .setAutoCancel(true)
                     .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setOngoing(true) // Make notification non-dismissible while playing
                    .build()

            // Start foreground service BEFORE showing notification to ensure it appears
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startForeground(2, notification!!, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
            } else {
                startForeground(2, notification)
            }
        }
    }

    /**
     * Update MediaSessionCompat metadata with current track information
     */
    private fun updateMediaSessionMetadata(nameReciter: String, nameSurah: String, duration: Int) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, nameSurah)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, nameReciter)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, nameReciter)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, (duration * 1000).toLong()) // Convert to milliseconds
            .build()
        mediaSessionCompat.setMetadata(metadata)
    }

    /**
     * Update MediaSessionCompat playback state
     */
    private fun updateMediaSessionPlaybackState(currentPosition: Int, duration: Int, isPlaying: Boolean) {
        val playbackState = PlaybackStateCompat.Builder()
            .setState(
                if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                (currentPosition * 1000).toLong(), // Convert to milliseconds
                1.0f // Playback speed
            )
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                PlaybackStateCompat.ACTION_PAUSE or
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                PlaybackStateCompat.ACTION_SEEK_TO
            )
            .build()
        mediaSessionCompat.setPlaybackState(playbackState)
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release MediaSessionCompat to free resources
        mediaSessionCompat.release()
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

    /**
     * Handle play/pause directly in service when Activity is not bound
     */
    private fun handlePlayPauseDirectly() {
        if (mediaPlayer == null) return
        
        val isCurrentlyPlaying = mediaPlayer!!.isPlaying
        if (isCurrentlyPlaying) {
            pause()
        } else {
            start()
        }
        
        // Update notification with current state
        val currentPos = getCurrentPosition() / 1000
        val duration = getDuration()?.div(1000) ?: 0
        val playPauseIcon = if (mediaPlayer!!.isPlaying) R.drawable.pause_noti else R.drawable.play_noti
        showNotification(playPauseIcon, nameReciter, nameSurah, duration, currentPos)
    }

    /**
     * Handle next track directly in service when Activity is not bound
     */
    private fun handleNextDirectly() {
        // Try to use static reciations list if available
        val reciationsList = com.elramady.moshafy.Adapters.ReciationsAdapter.reciationsList
        if (reciationsList.isEmpty()) {
            Log.e("MusicService", "Cannot handle next - reciations list is empty")
            return
        }
        
        // Update position using same logic as Activity
        if (com.elramady.moshafy.ui.ReciationsActivity.shuffleBoolean && 
            !com.elramady.moshafy.ui.ReciationsActivity.repeatBoolean) {
            position = getRandomPosition(reciationsList.size - 1)
        } else if (!com.elramady.moshafy.ui.ReciationsActivity.shuffleBoolean && 
                   !com.elramady.moshafy.ui.ReciationsActivity.repeatBoolean) {
            position = (position + 1) % reciationsList.size
        }
        
        // Load next track
        if (position < reciationsList.size) {
            val nextUrl = reciationsList[position].url
            val wasPlaying = mediaPlayer?.isPlaying ?: false
            
            stop()
            release()
            createMediaPlayer(nextUrl)
            nameSurah = reciationsList[position].name
            
            // Update notification
            val duration = getDuration()?.div(1000) ?: 0
            val playPauseIcon = if (wasPlaying) R.drawable.pause_noti else R.drawable.play_noti
            showNotification(playPauseIcon, nameReciter, nameSurah, duration, 0)
            
            if (wasPlaying) {
                start()
                showNotification(R.drawable.pause_noti, nameReciter, nameSurah, duration, 0)
            }
            
            onCompleted()
        }
    }

    /**
     * Handle previous track directly in service when Activity is not bound
     */
    private fun handlePreviousDirectly() {
        // Try to use static reciations list if available
        val reciationsList = com.elramady.moshafy.Adapters.ReciationsAdapter.reciationsList
        if (reciationsList.isEmpty()) {
            Log.e("MusicService", "Cannot handle previous - reciations list is empty")
            return
        }
        
        // Update position using same logic as Activity
        if (com.elramady.moshafy.ui.ReciationsActivity.shuffleBoolean && 
            !com.elramady.moshafy.ui.ReciationsActivity.repeatBoolean) {
            position = getRandomPosition(reciationsList.size - 1)
        } else if (!com.elramady.moshafy.ui.ReciationsActivity.shuffleBoolean && 
                   !com.elramady.moshafy.ui.ReciationsActivity.repeatBoolean) {
            if (position - 1 < 0) {
                position = reciationsList.size - 1
            } else {
                position = position - 1
            }
        }
        
        // Load previous track
        if (position < reciationsList.size) {
            val prevUrl = reciationsList[position].url
            val wasPlaying = mediaPlayer?.isPlaying ?: false
            
            stop()
            release()
            createMediaPlayer(prevUrl)
            nameSurah = reciationsList[position].name
            
            // Update notification
            val duration = getDuration()?.div(1000) ?: 0
            val playPauseIcon = if (wasPlaying) R.drawable.pause_noti else R.drawable.play_noti
            showNotification(playPauseIcon, nameReciter, nameSurah, duration, 0)
            
            if (wasPlaying) {
                start()
                showNotification(R.drawable.pause_noti, nameReciter, nameSurah, duration, 0)
            }
            
            onCompleted()
        }
    }

    /**
     * Helper method to get random position (same logic as Activity)
     */
    private fun getRandomPosition(max: Int): Int {
        val random = java.util.Random()
        return random.nextInt(max + 1)
    }

    /**
     * Update notification progress without recreating the entire notification
     * This is more efficient for frequent updates
     */
    @SuppressLint("ForegroundServiceType")
    fun updateNotificationProgress(progress: Int, max: Int, isPlaying: Boolean) {
        if (notification == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        // Update MediaSessionCompat playback state
        updateMediaSessionPlaybackState(progress, max, isPlaying)

        val playPauseBtn = if (isPlaying) R.drawable.pause_noti else R.drawable.play_noti

        val prevIntent = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_PREVIOUS)
        val prevPending: PendingIntent = PendingIntent
            .getBroadcast(this, 0, prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_PLAY)
        val pausePending: PendingIntent = PendingIntent
            .getBroadcast(this, 0, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val nextIntent = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_NEXT)
        val nextPending: PendingIntent = PendingIntent
            .getBroadcast(this, 0, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationIntent_Close: Intent = Intent(this, NotificationReceiver::class.java)
            .setAction(ApplicationClass.ACTION_CLOSE)
        val closePending: PendingIntent = PendingIntent.getBroadcast(this,
            0, notificationIntent_Close,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val intent = Intent(this, PlayerReciationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("url", url)
            putExtra("surah_Name", nameSurah)
            putExtra("position", position)
        }
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val icon = BitmapFactory.decodeResource(resources, R.drawable.photo_play)

        notification = NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID_2)
            .setSmallIcon(R.drawable.icon_logo)
            .setContentTitle(nameSurah)
            .setContentText(nameReciter)
            .setLargeIcon(icon)
            .setColor(Color.WHITE)
            .setProgress(max, progress, false)
            .setContentIntent(resultPendingIntent)
            .addAction(R.drawable.previous_audio, "Previous", prevPending)
            .addAction(playPauseBtn, "Pause", pausePending)
            .addAction(R.drawable.next_audio, "next", nextPending)
            .addAction(R.drawable.ic_close, "Close", closePending)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setMediaSession(mediaSessionCompat.sessionToken)
                .setShowActionsInCompactView(0, 1, 2))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .build()

        // Update the existing notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(2, notification)
    }


}