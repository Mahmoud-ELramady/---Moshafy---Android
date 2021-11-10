package com.elramady.moshafy.FragmentPlayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    companion object{
        val CHANNEL_ID_1:String="channel1"
        val CHANNEL_ID_2:String="channel2"
        val ACTION_PREVIOUS="actionPrevious"
        val ACTION_NEXT="actionNext"
        val ACTION_PLAY="actionPlay"
        val ACTION_CLOSE= "actionClose"

    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel1=NotificationChannel(CHANNEL_ID_1,"Channel(1)",NotificationManager.IMPORTANCE_HIGH)
            channel1.description="Channel 1 Desc..."


            val channel2=NotificationChannel(CHANNEL_ID_2,"Channel(2)",NotificationManager.IMPORTANCE_HIGH)
            channel1.description="Channel 2 Desc..."


            val notificationManager:NotificationManager=getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)

        }
    }
}