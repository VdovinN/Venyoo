package com.app.venyoo.screens.login

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.inputmethod.EditorInfo
import com.app.venyoo.R
import com.app.venyoo.base.BaseActivity
import com.app.venyoo.extension.hideKeyboard
import com.app.venyoo.screens.login.structure.LoginPresenter
import com.app.venyoo.screens.login.structure.LoginView
import com.app.venyoo.screens.main.MainActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()

        presenter.takeView(this)
    }

    private fun setupView() {
        val content = SpannableString(rememberPasswordTextView.text)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        rememberPasswordTextView.text = content

        RxTextView.editorActionEvents(passwordEditText).subscribe {
            if (it.actionId() == EditorInfo.IME_ACTION_DONE) {
                val view = it.view()
                view.clearFocus()
                view.hideKeyboard()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.isAuthorized()
    }

    override fun getInputEmail(): String = emailEditText.text.toString()

    override fun getInputPassword(): String = passwordEditText.text.toString()

    override fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun getRememberState(): Boolean = checkBox.isChecked

    override fun error() {
        snackbar(rootView, "Неправильный логин или пароль")
    }

    override fun loginButtonClicked(): Observable<Pair<String, String>> =
            RxView.clicks(loginButton).map { Pair(emailEditText.text.toString(), passwordEditText.text.toString()) }
}
