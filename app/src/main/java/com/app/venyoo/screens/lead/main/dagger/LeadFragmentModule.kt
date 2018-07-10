package com.app.venyoo.screens.lead.main.dagger

import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.VenyooApi
import com.app.venyoo.rx.RxSchedulers
import com.app.venyoo.screens.lead.main.structure.LeadPresenter
import dagger.Module
import dagger.Provides

@Module
class LeadFragmentModule {

    @Provides
    fun providePresenter(api: VenyooApi, preferenceHelper: PreferenceHelper, rxSchedulers: RxSchedulers) = LeadPresenter(api, preferenceHelper, rxSchedulers)

}