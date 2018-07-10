package com.app.venyoo.application.dagger

import com.app.venyoo.application.VenyooApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    BuildersModule::class,
    NetworkModule::class
])
interface AppComponent {

    fun inject(app: VenyooApp)

}