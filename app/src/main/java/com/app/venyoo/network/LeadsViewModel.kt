package com.app.venyoo.network

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.rx.RxSchedulers
import io.reactivex.disposables.CompositeDisposable

class LeadsViewModel(private val api: VenyooApi, private val preferenceHelper: PreferenceHelper, private val rxSchedulers: RxSchedulers) : ViewModel() {

    var leadList: LiveData<PagedList<Lead>>

    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 4

    private val sourceFactory: LeadsDataSourceFactory

    init {
        sourceFactory = LeadsDataSourceFactory(compositeDisposable, api, preferenceHelper, rxSchedulers)
        val config = PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize * 2)
                .setEnablePlaceholders(false)
                .build()
        leadList = LivePagedListBuilder<Int, Lead>(sourceFactory, config).build()

    }

    fun retry() {
        sourceFactory.leadsDataSourceLiveData.value?.retry()
    }

    fun refresh() {
        sourceFactory.leadsDataSourceLiveData.value?.invalidate()
    }


    fun getNetworkState(): LiveData<NetworkState> = Transformations.switchMap<LeadsDataSource, NetworkState>(
            sourceFactory.leadsDataSourceLiveData) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> = Transformations.switchMap<LeadsDataSource, NetworkState>(
            sourceFactory.leadsDataSourceLiveData) { it.initialLoad }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}