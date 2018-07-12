package com.app.venyoo.screens.lead.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate
import com.app.venyoo.extension.underline
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.detail.structure.LeadDetailPresenter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailView
import com.app.venyoo.screens.main.MainActivity
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.lead_detail_layout.*
import javax.inject.Inject

class LeadDetailFragment : Fragment(), LeadDetailView {

    @Inject
    lateinit var presenter: LeadDetailPresenter

    companion object {

        const val LEAD: String = "LEAD"

        fun newInstance(lead: Lead): LeadDetailFragment {
            val fragment = LeadDetailFragment()
            val args = Bundle()
            args.putSerializable(LEAD, lead)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.lead_detail_layout)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).enableViews(true)

        presenter.lead = arguments?.get(LEAD) as Lead?



        presenter.takeView(this)
    }

    override fun displayLeadInfo(lead: Lead) {
        (activity as MainActivity).supportActionBar?.title = lead.firstLastName
        leadUserPhoneTextView.text = lead.phone

        lead.socialData?.let {
            Picasso.get().load(it.photo).into(leadUserImageView)
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

        leadUserSmsTextView.text = if (lead.sms == 0) getString(R.string.not_connected) else getString(R.string.connected)

        leadUserMailTextView.underline()
        leadUserPhoneFieldTextView.underline()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).enableViews(false)
    }

}