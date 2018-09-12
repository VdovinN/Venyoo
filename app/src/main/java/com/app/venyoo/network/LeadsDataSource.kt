package com.app.venyoo.network

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action


class LeadsDataSource(private val api: VenyooApi, private val preferenceHelper: PreferenceHelper, private val compositeDisposable: CompositeDisposable, private val rxSchedulers: RxSchedulers) : ItemKeyedDataSource<Int, Lead>() {

    private var pageNumber = 1

    private var retryCompletable: Completable? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Lead>) {
        compositeDisposable.add(api.getLeads(preferenceHelper.loadToken(), 1).subscribe({ users ->
            callback.onResult(users.data as MutableList<Lead>)
            pageNumber++
        }, { throwable -> setRetry(Action { loadInitial(params, callback) })
            val error = NetworkState.error(throwable.message)
            // publish the error
            networkState.postValue(error)
            initialLoad.postValue(error)
        }))
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Lead>) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        compositeDisposable.add(api.getLeads(preferenceHelper.loadToken(), params.key).subscribe({ users ->
            setRetry(null)
            networkState.postValue(NetworkState.LOADED)
            initialLoad.postValue(NetworkState.LOADED)
            callback.onResult(users.data as MutableList<Lead>)
            pageNumber++
        }, { throwable ->
            setRetry(Action { loadAfter(params, callback) })
            networkState.postValue(NetworkState.error(throwable.message))
        }))
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Lead>) {
    }

    override fun getKey(item: Lead): Int = pageNumber

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                    .subscribeOn(rxSchedulers.io())
                    .observeOn(rxSchedulers.androidUI())
                    .subscribe({ }, { throwable -> throwable.printStackTrace() }))
        }
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
}