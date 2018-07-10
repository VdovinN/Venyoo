package com.app.venyoo.screens.lead.main.structure

import com.app.venyoo.base.BaseView
import com.app.venyoo.network.model.Lead

interface LeadView : BaseView {

    fun displayLeads(leadList: MutableList<Lead>)

}