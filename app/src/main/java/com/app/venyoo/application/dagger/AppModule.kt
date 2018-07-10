package com.app.venyoo.application.dagger

import android.content.Context
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.rx.AppRxSchedulers
import com.app.venyoo.rx.RxSchedulers
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun providePreferenceHelper() = PreferenceHelper(context)

    @Provides
    fun provideRxSchedulers(): RxSchedulers = AppRxSchedulers()

}