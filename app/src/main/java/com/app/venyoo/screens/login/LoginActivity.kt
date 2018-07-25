package com.app.venyoo.screens.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.inputmethod.EditorInfo
import com.app.venyoo.R
import com.app.venyoo.base.BaseActivity
import com.app.venyoo.extension.hideKeyboard
import com.app.venyoo.extension.underline
import com.app.venyoo.network.NetworkEvent
import com.app.venyoo.screens.login.structure.LoginPresenter
import com.app.venyoo.screens.login.structure.LoginView
import com.app.venyoo.screens.main.MainActivity
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import dagger.android.AndroidInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject


class LoginActivity : BaseActivity(), LoginView {

    @Inject
    lateinit var presenter: LoginPresenter

    private var doubleBackToExitPressedOnce = false

    private var builder: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        builder = AlertDialog.Builder(this).setMessage("Ошибка! Проверьте интернет-соединение ").setCancelable(false)

        setupView()

        presenter.takeView(this)
    }

    override fun onStart() {
        super.onStart()

        Bus.observe<NetworkEvent>()
                .subscribe {
                    if (!it.isOnline) {
                        alertDialog = builder?.show()
                    } else {
                        alertDialog?.let {
                            if (it.isShowing) {
                                it.dismiss()
                            }
                        }
                    }
                }
                .registerInBus(this)
    }

    override fun onStop() {
        super.onStop()
        Bus.unregister(this)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true

            snackbar(rootView, R.string.exit_hint).duration = 2000

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupView() {
        rememberPasswordTextView.underline()

        RxTextView.editorActionEvents(passwordEditText).subscribe {
            if (it.actionId() == EditorInfo.IME_ACTION_DONE) {
                val view = it.view()
                view.clearFocus()
                view.hideKeyboard()
            }
        }
    }

    override fun getInputEmail(): String = emailEditText.text.toString()

    override fun getInputPassword(): String = passwordEditText.text.toString()

    override fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun error() {
        snackbar(rootView, getString(R.string.login_error_message))
    }

    override fun emptyFields() {
        snackbar(rootView, getString(R.string.empty_fields))
    }

    override fun loginButtonClicked(): Observable<Pair<String, String>> =
            RxView.clicks(loginButton).map { Pair(emailEditText.text.toString(), passwordEditText.text.toString()) }
}
