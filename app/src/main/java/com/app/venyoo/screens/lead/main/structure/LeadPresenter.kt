package com.app.venyoo.screens.lead.main.structure

import com.app.venyoo.base.BasePresenter
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.VenyooApi
import com.app.venyoo.rx.RxSchedulers
import javax.inject.Inject

class LeadPresenter @Inject constructor(val api: VenyooApi, val preferenceHelper: PreferenceHelper, val rxSchedulers: RxSchedulers) : BasePresenter<LeadView>() {

    override fun onLoad() {
        super.onLoad()
        disposables.add(click())
    }

    private fun click() = getView().leadClicked().subscribe { getView().openLeadDetail(it) }

}