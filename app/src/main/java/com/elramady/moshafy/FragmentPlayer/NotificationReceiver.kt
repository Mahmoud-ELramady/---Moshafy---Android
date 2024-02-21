package com.elramady.moshafy.FragmentPlayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_CLOSE
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_PLAY
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_NEXT
import com.elramady.moshafy.FragmentPlayer.ApplicationClass.Companion.ACTION_PREVIOUS

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val actionName=intent?.action
        val serviceIntent:Intent=Intent(context,MusicService::class.java)
        if (actionName!=null){
            Log.e("recevier","Reccevier")
            Log.e("action",actionName+"jjj")

            when(actionName){
                ACTION_PLAY -> {
                        serviceIntent.putExtra("ActionName","playPause")
                        context?.startService(serviceIntent)
                    }


                ACTION_NEXT -> {
                    serviceIntent.putExtra("ActionName","Next")
                    context?.startService(serviceIntent)
                }



                ACTION_PREVIOUS -> {
                    serviceIntent.putExtra("ActionName","Previous")
                    context?.startService(serviceIntent)
                }


                ACTION_CLOSE -> {
                    serviceIntent.putExtra("ActionName","Close")
                    context?.startService(serviceIntent)
                }


            }
        }
    }
}