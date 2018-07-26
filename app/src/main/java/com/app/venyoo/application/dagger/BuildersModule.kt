package com.app.venyoo.application.dagger

import com.app.venyoo.screens.lead.detail.LeadDetailActivity
import com.app.venyoo.screens.lead.main.LeadFragment
import com.app.venyoo.screens.lead.main.dagger.LeadFragmentModule
import com.app.venyoo.screens.login.LoginActivity
import com.app.venyoo.screens.login.dagger.LoginModule
import com.app.venyoo.screens.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [LoginModule::class])
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [LeadFragmentModule::class])
    abstract fun bindLeadFragment(): LeadFragment

    @ContributesAndroidInjector
    abstract fun bindLeadDetailActivity(): LeadDetailActivity

}