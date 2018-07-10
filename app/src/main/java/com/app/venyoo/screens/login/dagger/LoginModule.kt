package com.app.venyoo.screens.login.dagger

import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.VenyooApi
import com.app.venyoo.rx.RxSchedulers
import com.app.venyoo.screens.login.structure.LoginPresenter
import dagger.Module
import dagger.Provides

@Module
class LoginModule {

    @Provides
    fun providePresenter(api: VenyooApi, preferenceHelper: PreferenceHelper, rxSchedulers: RxSchedulers) = LoginPresenter(api, preferenceHelper, rxSchedulers)

}