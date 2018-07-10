package com.app.venyoo.screens.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate

class SettingsFragment : Fragment() {

    companion object {
        val TAG: String = SettingsFragment::class.java.simpleName
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.settings_layout)
}
