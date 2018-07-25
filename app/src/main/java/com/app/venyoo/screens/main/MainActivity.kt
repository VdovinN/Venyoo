package com.app.venyoo.screens.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import android.view.View
import com.app.venyoo.R
import com.app.venyoo.base.BaseActivity
import com.app.venyoo.extension.findFragment
import com.app.venyoo.helper.NavigationPosition
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.helper.findNavigationPositionById
import com.app.venyoo.helper.getTag
import com.app.venyoo.network.NetworkEvent
import com.app.venyoo.screens.login.LoginActivity
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_layout.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import javax.inject.Inject


class MainActivity : BaseActivity(), HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var doubleBackToExitPressedOnce = false

    private var navPosition: NavigationPosition = NavigationPosition.LEADS

    private var mToolBarNavigationListenerIsRegistered = false

    private var builder: AlertDialog.Builder? = null
    private var alertDialog: AlertDialog? = null

    private val drawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        builder = AlertDialog.Builder(this).setMessage("Ошибка! Проверьте интернет-соединение ").setCancelable(false)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle.isDrawerIndicatorEnabled = true

        val email = preferenceHelper.loadEmail()


        navigationView.setNavigationItemSelectedListener(this)
        navigationView.menu.getItem(0).isChecked = true
        supportActionBar?.title = getString(R.string.leads)

        email.let {
            navigationView.getHeaderView(0).emailTextView.text = it
        }

        initFragment(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
            when {
                drawerToggle.onOptionsItemSelected(item) -> true
                item.itemId == android.R.id.home -> {
                    onBackPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when {
            item.groupId == R.id.main_group -> {
                navPosition = findNavigationPositionById(item.itemId)
                when (navPosition.position) {
                    0 -> supportActionBar?.title = getString(R.string.leads)
                    1 -> supportActionBar?.title = getString(R.string.chat)
                    2 -> supportActionBar?.title = getString(R.string.settings)
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                switchFragment(navPosition)
            }
            else -> {
                exitAlert()
                true
            }
        }
    }

    private fun exitAlert() {
        alert(getString(R.string.do_you_want_to_exit)) {
            positiveButton(getString(R.string.yes)) { dialog ->
                preferenceHelper.clearEmail()
                preferenceHelper.clearPassword()
                preferenceHelper.clearToken()
                dialog.dismiss()
                goToLogin()
            }
            negativeButton(getString(R.string.no)) { dialog -> dialog.dismiss() }
        }.show()
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun switchFragment(navPosition: NavigationPosition): Boolean {
        val fragment = supportFragmentManager.findFragment(navPosition)
        if (fragment.isAdded) return false
        detachFragment()
        attachFragment(fragment, navPosition.getTag())
        supportFragmentManager.executePendingTransactions()
        return true
    }

    private fun detachFragment() {
        supportFragmentManager.findFragmentById(R.id.container)?.also {
            supportFragmentManager.beginTransaction().detach(it).commit()
        }
    }

    private fun attachFragment(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            supportFragmentManager.beginTransaction().attach(fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.container, fragment, tag).commit()
        }
        supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(NavigationPosition.LEADS)
    }

    fun enableViews(enable: Boolean) {
        if (enable) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            drawerToggle.isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (!mToolBarNavigationListenerIsRegistered) {
                drawerToggle.toolbarNavigationClickListener = View.OnClickListener { onBackPressed() }
                mToolBarNavigationListenerIsRegistered = true
            }
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            drawerToggle.isDrawerIndicatorEnabled = true
            drawerToggle.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
        }
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true

            snackbar(container, R.string.exit_hint).duration = 2000

            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}
