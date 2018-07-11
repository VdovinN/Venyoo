package com.app.venyoo.screens.lead.detail.dagger

import com.app.venyoo.screens.lead.detail.structure.LeadDetailPresenter
import dagger.Module
import dagger.Provides

@Module
class LeadDetailFragmentModule {

    @Provides
    fun providePresenter(): LeadDetailPresenter = LeadDetailPresenter()

}