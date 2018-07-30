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

    override fun onStart() {
        super.onStart()
        registerNetworkBroadcastForNougat()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkChanges()
    }

    /*override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        val v = currentFocus

         if (v != null &&
                 (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                 v is EditText &&
                 !v.javaClass.name.startsWith("android.webkit.")) {
             val scrcoords = IntArray(2)
             v.getLocationOnScreen(scrcoords)
             val x = ev.rawX + v.left - scrcoords[0]
             val y = ev.rawY + v.top - scrcoords[1]

             if (x < v.left || x > v.right || y < v.top || y > v.bottom) {
                 v.hideKeyboard()
                 v.clearFocus()
             }
         }
        return super.dispatchTouchEvent(ev)
    }*/

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