package com.app.venyoo.screens.lead.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.detail.structure.LeadDetailPresenter
import com.app.venyoo.screens.lead.detail.structure.LeadDetailView
import com.app.venyoo.screens.main.MainActivity
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
        leadUserPhoneTextView.text = lead.phone
        leadUserPhoneFieldTextView.text = lead.phone
        leadUserCityTextView.text = lead.region
        leadUserQuestionTextView.text = lead.question
        leadUserDateTextView.text = lead.createdAt?.let { DateHelper.formatExactDate(it) }
        leadUserMailTextView.text = lead.email
        leadUserSmsTextView.text = lead.sms.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).enableViews(false)
    }

}