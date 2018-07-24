package com.app.venyoo.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.widget.EditText
import com.app.venyoo.extension.hideKeyboard

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus

        if (v != null &&
                (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                v is EditText &&
                !v.javaClass.name.startsWith("android.webkit.")) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.left - scrcoords[0]
            val y = ev.rawY + v.top - scrcoords[1]

            if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                v.hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }
}