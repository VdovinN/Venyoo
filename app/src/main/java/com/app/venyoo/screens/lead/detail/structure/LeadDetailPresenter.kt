package com.app.venyoo.screens.lead.detail.structure

import com.app.venyoo.base.BasePresenter
import com.app.venyoo.network.model.Lead
import javax.inject.Inject

class LeadDetailPresenter @Inject constructor() : BasePresenter<LeadDetailView>() {

    var lead: Lead? = null

    override fun onLoad() {
        super.onLoad()
        lead?.let {
            getView().displayLeadInfo(it)
            call(it.phone)
        }
    }

    private fun call(phone: String) = getView().callButtonClicked().subscribe { getView().call(phone) }

}