package com.elramady.moshafy.FragmentPlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_CLOSE
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_PLAY
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_NEXT
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_PREVIOUS

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionName=intent?.action
        val serviceIntent:Intent=Intent(context,MusicService::class.java)
        if (actionName!=null && context != null){
            Log.e("recevier","Reccevier")
            Log.e("action",actionName+"jjj")

            when(actionName){
                ACTION_PLAY -> {
                        serviceIntent.putExtra("ActionName","playPause")
                        startMusicService(context, serviceIntent)
                    }


                ACTION_NEXT -> {
                    serviceIntent.putExtra("ActionName","Next")
                    startMusicService(context, serviceIntent)
                }



                ACTION_PREVIOUS -> {
                    serviceIntent.putExtra("ActionName","Previous")
                    startMusicService(context, serviceIntent)
                }


                ACTION_CLOSE -> {
                    serviceIntent.putExtra("ActionName","Close")
                    startMusicService(context, serviceIntent)
                }


            }
        }
    }

    /**
     * Use startForegroundService on API 26+ so notification actions are delivered
     * when app is in background; otherwise startService can be blocked.
     */
    private fun startMusicService(context: Context, serviceIntent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}