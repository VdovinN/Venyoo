package com.app.venyoo.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.eightbitlab.rxbus.Bus

class NetworkReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        if (isOnline(context)) {
            Bus.send(NetworkEvent(true))
        } else {
            Bus.send(NetworkEvent(false))
        }
    }

    private fun isOnline(context: Context?): Boolean {
        return try {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }

    }

}