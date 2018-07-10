package com.app.venyoo.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class AppRxSchedulers : RxSchedulers {

    companion object {
        private val NETWORK_EXECUTOR = Executors.newCachedThreadPool()
        private val NETWORK_SCHEDULER = Schedulers.from(NETWORK_EXECUTOR)
    }

    override fun androidUI(): Scheduler = AndroidSchedulers.mainThread()

    override fun io(): Scheduler = Schedulers.io()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun network(): Scheduler = NETWORK_SCHEDULER

    override fun trampoline(): Scheduler = Schedulers.trampoline()

}