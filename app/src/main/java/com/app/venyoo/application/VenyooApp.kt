package com.app.venyoo.application

import android.app.Activity
import android.app.Application
import com.app.venyoo.application.dagger.AppComponent
import com.app.venyoo.application.dagger.AppModule
import com.app.venyoo.application.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class VenyooApp : Application(), HasActivityInjector {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(applicationContext))
                .build()

        appComponent.inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector


}