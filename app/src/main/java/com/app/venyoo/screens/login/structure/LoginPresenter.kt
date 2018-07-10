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
                            getView().startMain()
                        }
                    } else if (it.code() == Constants.RESPONSE_UNAUTHORIZED) {
                        getView().error()
                    }
                },
                        { it.printStackTrace() })
    }

    public fun isAuthorized(): Disposable = Observable.just(preferenceHelper.loadToken() != null).filter { it }.subscribe { getView().startMain() }

}