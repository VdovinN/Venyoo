package com.app.venyoo.screens.chat

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate

class ChatFragment : Fragment() {

    companion object {
        val TAG: String = ChatFragment::class.java.simpleName
        fun newInstance(): ChatFragment = ChatFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.chat_fragment)


}