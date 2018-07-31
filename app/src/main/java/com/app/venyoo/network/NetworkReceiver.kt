package com.app.venyoo.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v7.app.AlertDialog
import com.app.venyoo.R

class NetworkReceiver : BroadcastReceiver() {

    private var alertDialog: AlertDialog? = null

    override fun onReceive(context: Context, intent: Intent?) {

        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(context).setMessage(context.getString(R.string.network_problems)).setCancelable(false).create()
        }
        if (!isOnline(context)) {
            alertDialog?.show()
        } else {
            alertDialog?.dismiss()
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