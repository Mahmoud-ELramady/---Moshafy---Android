package com.elramady.moshafy

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.util.Log

object ConnectivityUtil {
    /**
     * Get the network info
     * @param context
     * @return
     */
    fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    fun isConnected(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     *
     * @return
     */
    fun isConnectedWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     *
     * @return
     */
    fun isConnectedMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    fun isConnectedFast(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isConnected && isConnectionFast(
                info.type,
                info.subtype,context
        )
    }
    /**
     * Check if the connection is fast
     *
     * @param subType
     * @return
     */
    fun isConnectionFast(type: Int, subType: Int,context: Context): Boolean {
        return if (type == ConnectivityManager.TYPE_WIFI) {

           val wifiManager: WifiManager  = context.getSystemService(Context.WIFI_SERVICE) as (WifiManager)
            val numberOfLevels:Int = 7
           val wifiInfo: WifiInfo  = wifiManager.connectionInfo
            val  level:Int = WifiManager.calculateSignalLevel(wifiInfo.rssi, numberOfLevels)
            val ressi:Int=wifiManager.connectionInfo.rssi
            Log.e("speed",ressi.toString())
            if(level == 2 ){
                Log.e("state",level.toString())
                return true
            } else if(level == 3 ){
                Log.e("state",level.toString())

                return true
            } else if(level == 4 ){
                Log.e("state",level.toString())

                return true

            } else if(level == 5 ){
                Log.e("state",level.toString())

                return true
            }else if(level == 6 ){
                Log.e("state",level.toString())

                return true
            }
            else{
                Log.e("state",level.toString())
                return false

            }

        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            Log.e("subType",subType.toString())
            when (subType) {

                TelephonyManager.NETWORK_TYPE_1xRTT -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_CDMA -> false // ~ 14-64 kbps
                TelephonyManager.NETWORK_TYPE_EDGE -> false // ~ 50-100 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_0 -> true // ~ 400-1000 kbps
                TelephonyManager.NETWORK_TYPE_EVDO_A -> true // ~ 600-1400 kbps
                TelephonyManager.NETWORK_TYPE_GPRS -> false // ~ 100 kbps
                TelephonyManager.NETWORK_TYPE_HSDPA -> true // ~ 2-14 Mbps
                TelephonyManager.NETWORK_TYPE_HSPA -> true // ~ 700-1700 kbps
                TelephonyManager.NETWORK_TYPE_HSUPA -> true // ~ 1-23 Mbps
                TelephonyManager.NETWORK_TYPE_UMTS -> true // ~ 400-7000 kbps
                TelephonyManager.NETWORK_TYPE_EHRPD -> true // ~ 1-2 Mbps
                TelephonyManager.NETWORK_TYPE_EVDO_B -> true // ~ 5 Mbps
                TelephonyManager.NETWORK_TYPE_HSPAP -> true // ~ 10-20 Mbps
                TelephonyManager.NETWORK_TYPE_IDEN -> false // ~25 kbps
                TelephonyManager.NETWORK_TYPE_LTE -> true // ~ 10+ Mbps
                TelephonyManager.NETWORK_TYPE_UNKNOWN -> false
                else -> false
            }
        } else {
            false
        }
    }
}

//
//<FrameLayout
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:layout_gravity="bottom"
//>
//
//<fragment
//android:layout_gravity="bottom"
//android:id="@+id/fragment_play_reciations"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//tools:layout="@layout/fragment_playing_now"
//android:name="com.example.quany.FragmentPlayer.PlayingNowFragment"
///>
//
//</FrameLayout>