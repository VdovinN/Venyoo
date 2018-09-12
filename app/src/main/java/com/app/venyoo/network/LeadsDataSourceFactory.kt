package com.app.venyoo.network

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.rx.RxSchedulers
import io.reactivex.disposables.CompositeDisposable

class LeadsDataSourceFactory(private val compositeDisposable: CompositeDisposable, private val api: VenyooApi, private val preferenceHelper: PreferenceHelper, private val rxSchedulers: RxSchedulers) : DataSource.Factory<Int, Lead>() {

    val leadsDataSourceLiveData = MutableLiveData<LeadsDataSource>()

    override fun create(): DataSource<Int, Lead> {
        val leadsDataSource = LeadsDataSource(api, preferenceHelper, compositeDisposable, rxSchedulers)
        leadsDataSourceLiveData.postValue(leadsDataSource)
        return leadsDataSource
    }

}