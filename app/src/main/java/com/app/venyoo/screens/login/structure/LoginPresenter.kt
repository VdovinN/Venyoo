package com.app.venyoo.screens.login.structure

import com.app.venyoo.Constants
import com.app.venyoo.base.BasePresenter
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.network.VenyooApi
import com.app.venyoo.rx.RxSchedulers
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

class LoginPresenter(private val api: VenyooApi, private val preferenceHelper: PreferenceHelper, private val rxSchedulers: RxSchedulers) : BasePresenter<LoginView>() {

    override fun onLoad() {
        super.onLoad()
        disposables.add(login())
    }

    private fun login(): Disposable {
        return getView().loginButtonClicked()
                .observeOn(rxSchedulers.io())
                .filter { !it.first.isEmpty() && !it.second.isEmpty() }
                .flatMap { api.login(it.first, it.second) }
                .observeOn(rxSchedulers.androidUI())
                .subscribe({
                    if (it.code() == Constants.RESPONSE_OK) {
                        val login = it.body()
                        login?.let {
                            preferenceHelper.saveToken(it.token)
                            preferenceHelper.saveEmail(getView().getInputEmail())
                            preferenceHelper.savePassword(getView().getInputPassword())
                            preferenceHelper.saveRememberUser(getView().getRememberState())
                            getView().startMain()
                        }
                    } else if (it.code() == Constants.RESPONSE_UNAUTHORIZED) {
                        getView().error()
                    }
                },
                        { it.printStackTrace() })
    }

    fun isAuthorized(): Disposable = Observable.just(preferenceHelper.loadRememberUser()).filter { it }.flatMap { api.login(preferenceHelper.loadEmail(), preferenceHelper.loadPassword()) }
            .subscribeOn(rxSchedulers.io())
            .observeOn(rxSchedulers.androidUI())
            .subscribe({
                if (it.code() == Constants.RESPONSE_OK) {
                    val login = it.body()
                    login?.let {
                        preferenceHelper.saveToken(it.token)
                        getView().startMain()
                    }
                } else if (it.code() == Constants.RESPONSE_UNAUTHORIZED) {
                    getView().error()
                }
            },
                    { it.printStackTrace() })


}