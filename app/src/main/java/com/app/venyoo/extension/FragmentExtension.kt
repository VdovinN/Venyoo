package com.app.venyoo.extension

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.app.venyoo.helper.NavigationPosition
import com.app.venyoo.helper.createFragment
import com.app.venyoo.helper.getTag

fun FragmentManager.findFragment(position: NavigationPosition): Fragment =
        findFragmentByTag(position.getTag())
                ?: position.createFragment()

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}