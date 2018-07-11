package com.app.venyoo.screens.lead.main.structure

import com.app.venyoo.base.BasePresenter
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.VenyooApi
import com.app.venyoo.rx.RxSchedulers
import javax.inject.Inject

class LeadPresenter @Inject constructor(private val api: VenyooApi, private val preferenceHelper: PreferenceHelper, private val rxSchedulers: RxSchedulers) : BasePresenter<LeadView>() {

    override fun onLoad() {
        super.onLoad()
        disposables.add(loadLeads())
        disposables.add(click())
    }

    private fun click() = getView().leadClicked().subscribe { getView().openLeadDetail(it) }

    private fun loadLeads() =
            api.getLeads(preferenceHelper.loadToken())
                    .subscribeOn(rxSchedulers.io())
                    .observeOn(rxSchedulers.androidUI())
                    .subscribe {
                        it.data?.let { it1 -> getView().displayLeads(it1) }
                    }

}