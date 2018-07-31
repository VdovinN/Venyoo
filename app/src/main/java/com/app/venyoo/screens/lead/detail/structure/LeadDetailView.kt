package com.app.venyoo.screens.lead.detail.structure

import com.app.venyoo.base.BaseView
import com.app.venyoo.network.model.Lead
import io.reactivex.Observable

interface LeadDetailView : BaseView{

    fun displayLeadInfo(lead: Lead)

    fun callButtonClicked(): Observable<Any>
    fun call(number: String)
}