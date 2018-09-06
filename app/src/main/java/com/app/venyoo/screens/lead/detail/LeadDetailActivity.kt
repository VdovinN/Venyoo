package com.app.venyoo.screens.lead.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.view.View
import com.amulyakhare.textdrawable.TextDrawable
import com.app.venyoo.R
import com.app.venyoo.base.BaseActivity
import com.app.venyoo.extension.dpToPx
import com.app.venyoo.extension.intToRGB
import com.app.venyoo.extension.underline
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.detail.adapter.LeadDetailSpinnerAdapter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailPresenter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailView
import com.jakewharton.rxbinding2.view.RxView
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.lead_detail_layout.*
import java.lang.Exception
import javax.inject.Inject

class LeadDetailActivity : BaseActivity(), LeadDetailView {

    @Inject
    lateinit var presenter: LeadDetailPresenter

    private lateinit var mConfig: SlidrConfig

    companion object {
        const val LEAD: String = "LEAD"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        setContentView(R.layout.lead_detail_layout)

        val secondary = ContextCompat.getColor(this, R.color.redStatusBarColor)

        mConfig = SlidrConfig.Builder()
                .primaryColor(secondary)
                .secondaryColor(secondary)
                .position(SlidrPosition.LEFT)
                .velocityThreshold(2400f)
                .touchSize(32.dpToPx())
                .build()

        Slidr.attach(this, mConfig)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.lead = intent.getSerializableExtra(LEAD) as Lead?

        presenter.takeView(this)

    }

    override fun displayLeadInfo(lead: Lead) {
        when {
            !lead.firstLastName.isEmpty() -> leadUserTitleTextView.text = lead.firstLastName
            !lead.phone.isEmpty() -> leadUserTitleTextView.text = lead.phone
            else -> leadUserTitleTextView.text = lead.email
        }

        leadUserCityTextView.text = lead.region
        leadUserQuestionTextView.text = lead.question
        leadUserDateTextView.text = lead.createdAt.let { DateHelper.formatExactDate(it) }

        if (lead.geotag != null) {
            lead.geotag?.let {
                leadUserCityByIpTextView.text = it.city
            }
        } else {
            leadUserCityByIpLineView.visibility = View.GONE
            leadUserCityByIpTextView.visibility = View.GONE
            leadUserCityByIpTitleTextView.visibility = View.GONE
        }


        leadUserDeviceTextView.text = if (lead.isMobile == 0) getString(R.string.from_computer) else getString(R.string.from_mobile)

        if (lead.region.isEmpty()) {
            leadUserTownTextView.visibility = View.GONE
            leadUserTownTitleTextView.visibility = View.GONE
            leadUserTownLineView.visibility = View.GONE
        } else {
            leadUserTownTextView.text = lead.region
        }

        if (lead.socialData != null) {
            lead.socialData?.let {
                Picasso.get().load(it.photo).into(leadUserImageView, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError(e: Exception?) {
                        displayDefaultImage(lead)
                    }
                })
                leadUserSocialProfileTextView.text = it.profile
            }

        } else {
            leadUserSocialProfileTitleTextView.visibility = View.GONE
            leadUserSocialProfileTextView.visibility = View.GONE
            leadUserSocialProfileLineView.visibility = View.GONE
        }

        if (lead.phone.isEmpty()) {
            leadUserPhoneFieldTextView.visibility = View.GONE
            leadUserPhoneFieldTitleTextView.visibility = View.GONE
            leadUserPhoneFieldLineView.visibility = View.GONE
            callButton.visibility = View.GONE
        } else {
            leadUserPhoneFieldTextView.text = lead.phone
        }

        if (lead.email.isEmpty()) {
            leadUserMailTextView.visibility = View.GONE
            leadUserMailTitleTextView.visibility = View.GONE
            leadUserMailLineView.visibility = View.GONE
        } else {
            leadUserMailTextView.text = lead.email
        }

        leadUserSeenTextView.text = when (lead.show) {
            "email" -> {
                val emailTxt = getString(R.string.from_email)
                if (lead.showTs > 0) "$emailTxt ${DateHelper.formatTime(lead.showTs)}" else emailTxt
            }
            "crm" -> {
                val crmTxt = getString(R.string.from_crm)
                if (lead.showTs > 0) "$crmTxt ${DateHelper.formatTime(lead.showTs)}" else crmTxt
            }
            else -> getString(R.string.not_seen)
        }

        if (lead.url.isEmpty()) {
            leadUserUrlTitleTextView.visibility = View.GONE
            leadUserUrlTextView.visibility = View.GONE
            leadUserUrlLineView.visibility = View.GONE
        } else {
            leadUserUrlTextView.text = lead.url
        }

        val statusPairList = listOf(
                Pair("new", "Не обработан"),
                Pair("connect_fail", "Не удалось связаться"),
                Pair("help_question", "Справочный вопрос"),
                Pair("spam", "Спам"),
                Pair("not_interested", "Не заинтересован в услуге"),
                Pair("deal", "Договорились о встрече"),
                Pair("processed", "Обработан"),
                Pair("send_mail", "Отправил письмо"),
                Pair("got_mail", "Получил ответ на e-mail"),
                Pair("open", "Открытый лид"),
                Pair("in_progress", "В обработке"))

        leadUserStatusSpinner.adapter = LeadDetailSpinnerAdapter(this, statusPairList)

        leadUserSmsTextView.text = if (lead.sms == 0) getString(R.string.not_connected) else getString(R.string.connected)

        leadUserMailTextView.underline()
        leadUserPhoneFieldTextView.underline()

    }

    private fun displayDefaultImage(lead: Lead) {
        val title = when {
            !lead.firstLastName.isEmpty() -> lead.firstLastName
            !lead.phone.isEmpty() -> lead.phone.substring(1)
            else -> lead.email
        }

        val result = if (title.isNotEmpty()) title[0].toUpperCase().toString() else ""

        val textDrawable = TextDrawable.builder().beginConfig().width(60).height(60).endConfig().buildRect(result, title.hashCode().intToRGB())
        leadUserImageView.setImageDrawable(textDrawable)
    }

    override fun callButtonClicked() = RxView.clicks(callButton)

    override fun call(number: String) {
        val callIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null))
        startActivity(callIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
    }

}