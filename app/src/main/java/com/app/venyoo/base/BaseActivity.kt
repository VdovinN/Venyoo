package com.app.venyoo.base

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import com.app.venyoo.network.NetworkReceiver

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    companion object {

        private const val ANDROID_NET_WIFI_WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED"
        private const val ANDROID_NET_WIFI_STATE_CHANGE = "android.net.wifi.STATE_CHANGE"

    }

    private val networkReceiver = NetworkReceiver()

    override fun onResume() {
        super.onResume()
        registerNetworkBroadcastForNougat()
    }

    override fun onPause() {
        super.onPause()
        unregisterNetworkChanges()
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        } else {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            filter.addAction(ANDROID_NET_WIFI_WIFI_STATE_CHANGED)
            filter.addAction(ANDROID_NET_WIFI_STATE_CHANGE)
            registerReceiver(networkReceiver, filter)
        }
    }

    private fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(networkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

}