package com.app.venyoo.screens.lead.detail

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.amulyakhare.textdrawable.TextDrawable
import com.app.venyoo.R
import com.app.venyoo.extension.dpToPx
import com.app.venyoo.extension.underline
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.detail.adapter.LeadDetailSpinnerAdapter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailPresenter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailView
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.lead_detail_layout.*
import java.util.*
import javax.inject.Inject

class LeadDetailActivity : AppCompatActivity(), LeadDetailView {

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

        // val primary = ContextCompat.getColor(this, R.color.generalRed)
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
            lead.firstLastName != null -> leadUserTitleTextView.text = lead.firstLastName
            lead.phone != null -> leadUserTitleTextView.text = lead.phone
            else -> leadUserTitleTextView.text = lead.email
        }

        if (lead.socialData != null) {
            lead.socialData?.let {
                Picasso.get().load(it.photo).into(leadUserImageView)
            }
        } else {
            val title: String = when {
                lead.firstLastName != null -> lead.firstLastName ?: ""
                lead.phone != null -> lead.phone ?: ""
                else -> lead.email ?: ""
            }

            val rnd = Random()
            val randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            val textDrawable = TextDrawable.builder().beginConfig().width(60).height(60).endConfig().buildRect(if (title.isNotEmpty()) title[0].toString() else "", randomColor)
            leadUserImageView.setImageDrawable(textDrawable)
        }

        leadUserCityTextView.text = lead.region
        leadUserQuestionTextView.text = lead.question
        leadUserDateTextView.text = lead.createdAt?.let { DateHelper.formatExactDate(it) }
        lead.phone?.let {
            if (it.isEmpty()) {
                leadUserPhoneFieldTextView.visibility = View.GONE
                leadUserPhoneFieldTitleTextView.visibility = View.GONE
                leadUserPhoneFieldLineView.visibility = View.GONE
            } else {
                leadUserPhoneFieldTextView.text = lead.phone
            }
        }
        lead.email?.let {
            if (it.isEmpty()) {
                leadUserMailTextView.visibility = View.GONE
                leadUserMailTitleTextView.visibility = View.GONE
                leadUserMailLineView.visibility = View.GONE
            } else {
                leadUserMailTextView.text = it
            }
        }

        leadUserSeenTextView.text = when (lead.show) {
            "email" -> getString(R.string.from_email)
            "crm" -> getString(R.string.from_crm)
            else -> getString(R.string.not_seen)
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