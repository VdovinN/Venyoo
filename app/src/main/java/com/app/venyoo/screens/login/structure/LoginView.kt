package com.app.venyoo.screens.login.structure

import com.app.venyoo.base.BaseView
import io.reactivex.Observable

interface LoginView : BaseView {

    fun error()

    fun getInputEmail(): String

    fun getInputPassword(): String

    fun startMain()

    fun loginButtonClicked(): Observable<Pair<String, String>>

    fun emptyFields()
}