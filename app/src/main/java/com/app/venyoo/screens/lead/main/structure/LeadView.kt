package com.app.venyoo.screens.lead.main.structure

import com.app.venyoo.base.BaseView
import com.app.venyoo.network.model.Lead
import io.reactivex.Observable

interface LeadView : BaseView {

    fun displayLeads(leadList: MutableList<Lead>)

    fun openLeadDetail(lead: Lead)

    fun leadClicked(): Observable<Lead>

    fun swipeToResfresh(): Observable<Any>
    fun setRefreshing(isRefreshing: Boolean)
}