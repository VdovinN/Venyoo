package com.app.venyoo.screens.lead.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.extension.inflate
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import kotlinx.android.synthetic.main.lead_item_layout.view.*

class LeadAdapter(private var leadList: MutableList<Lead>) : RecyclerView.Adapter<LeadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.lead_item_layout))

    override fun getItemCount(): Int = leadList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lead = leadList[position]
        holder.bind(lead)
    }

    fun swap(leads: MutableList<Lead>) {
        leadList = ArrayList(leads)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(lead: Lead) {
            itemView.leadUserNameTextView.text = lead.firstLastName
            itemView.leadTimeTextView.text = lead.createdAt?.let { DateHelper.formatDate(it) }
            itemView.leadContentTextView.text = lead.question
        }
    }

}