package com.app.venyoo.helper

import android.support.v4.app.Fragment
import com.app.venyoo.R
import com.app.venyoo.screens.chat.ChatFragment
import com.app.venyoo.screens.lead.main.LeadFragment
import com.app.venyoo.screens.settings.SettingsFragment

enum class NavigationPosition(val position: Int, val id: Int) {
    LEADS(0, R.id.action_leads),
    CHAT(1, R.id.action_chat),
    SETTINGS(2, R.id.action_settings)
}

fun findNavigationPositionById(id: Int): NavigationPosition = when (id) {
    NavigationPosition.LEADS.id -> NavigationPosition.LEADS
    NavigationPosition.CHAT.id -> NavigationPosition.CHAT
    NavigationPosition.SETTINGS.id -> NavigationPosition.SETTINGS
    else -> NavigationPosition.LEADS
}

fun NavigationPosition.createFragment(): Fragment = when (this) {
    NavigationPosition.LEADS -> LeadFragment.newInstance()
    NavigationPosition.CHAT -> ChatFragment.newInstance()
    NavigationPosition.SETTINGS -> SettingsFragment.newInstance()
}

fun NavigationPosition.getTag(): String = when (this) {
    NavigationPosition.LEADS -> LeadFragment.TAG
    NavigationPosition.CHAT -> ChatFragment.TAG
    NavigationPosition.SETTINGS -> SettingsFragment.TAG
}