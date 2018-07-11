package com.app.venyoo.screens.main

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.app.venyoo.R
import com.app.venyoo.extension.findFragment
import com.app.venyoo.helper.NavigationPosition
import com.app.venyoo.helper.PreferenceHelper
import com.app.venyoo.helper.findNavigationPositionById
import com.app.venyoo.helper.getTag
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header_layout.view.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector, NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    private var navPosition: NavigationPosition = NavigationPosition.LEADS

    private var mToolBarNavigationListenerIsRegistered = false

    private val drawerToggle: ActionBarDrawerToggle by lazy {
        ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        navPosition = findNavigationPositionById(item.itemId)
        when (navPosition.position) {
            0 -> supportActionBar?.title = getString(R.string.leads)
            1 -> supportActionBar?.title = getString(R.string.chat)
            2 -> supportActionBar?.title = getString(R.string.settings)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return switchFragment(navPosition)
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

    public fun enableViews(enable: Boolean) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable) {
            //You may not want to open the drawer on swipe from the left in this case
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            // Remove hamburger
            drawerToggle.isDrawerIndicatorEnabled = false
            // Show back button
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                drawerToggle.toolbarNavigationClickListener = View.OnClickListener { onBackPressed() }

                mToolBarNavigationListenerIsRegistered = true
            }

        } else {
            //You must regain the power of swipe for the drawer.
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

            // Remove back button
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            // Show hamburger
            drawerToggle.isDrawerIndicatorEnabled = true
            // Remove the/any drawer toggle listener
            drawerToggle.toolbarNavigationClickListener = null
            mToolBarNavigationListenerIsRegistered = false
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector
}
