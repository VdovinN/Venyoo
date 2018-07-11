package com.app.venyoo.screens.lead.detail.structure

import com.app.venyoo.base.BaseView
import com.app.venyoo.network.model.Lead

interface LeadDetailView : BaseView{

    fun displayLeadInfo(lead: Lead)

}