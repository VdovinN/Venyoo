package com.app.venyoo.screens.lead.main.structure

import com.app.venyoo.base.BaseView
import com.app.venyoo.network.model.Lead
import io.reactivex.Observable

interface LeadView : BaseView {

    fun openLeadDetail(lead: Lead)

    fun leadClicked(): Observable<Lead>
}