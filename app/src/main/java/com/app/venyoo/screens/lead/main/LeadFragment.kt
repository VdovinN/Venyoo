package com.app.venyoo.screens.lead.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate
import com.app.venyoo.extension.leadDetail
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.main.adapter.LeadAdapter
import com.app.venyoo.screens.lead.main.structure.LeadPresenter
import com.app.venyoo.screens.lead.main.structure.LeadView
import com.app.venyoo.screens.main.MainActivity
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.lead_layout.*
import javax.inject.Inject


class LeadFragment : Fragment(), LeadView {

    @Inject
    lateinit var presenter: LeadPresenter

    private lateinit var adapter: LeadAdapter

    companion object {
        val TAG: String = LeadFragment::class.java.simpleName
        fun newInstance(): LeadFragment = LeadFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = container?.inflate(R.layout.lead_layout)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onViewCreated(view, savedInstanceState)

        leadRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = LeadAdapter(mutableListOf())
        leadRecyclerView.adapter = adapter


        (activity as MainActivity).supportActionBar?.title = getString(R.string.leads)

        context?.let {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(it, R.color.generalRed))
        }


        presenter.takeView(this)
    }

    override fun displayLeads(leadList: MutableList<Lead>) {
        adapter.swap(leadList)
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        refreshLayout.isRefreshing = isRefreshing
    }

    override fun swipeToResfresh(): Observable<Any> {
        return Observable.create {
            refreshLayout.setOnRefreshListener {
                it.onNext(Any())
                setRefreshing(false)
            }
        }
    }

    override fun leadClicked(): Observable<Lead> = adapter.itemClicked

    override fun openLeadDetail(lead: Lead) {
        startActivity(context?.leadDetail(lead))
    }
}