package com.app.venyoo.screens.lead.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.app.venyoo.R
import com.app.venyoo.extension.inflate
import com.app.venyoo.extension.intToRGB
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.lead_item_layout.view.*
import java.lang.Exception
import java.util.*

class LeadAdapter(private var leadList: MutableList<Lead>) : RecyclerView.Adapter<LeadAdapter.ViewHolder>() {

    private val TYPE_LOADING = -1
    private val TYPE_RECIPE = 1

    val itemClicked: PublishSubject<Lead> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when (viewType) {
        TYPE_LOADING -> ViewHolder(parent.inflate(R.layout.lead_item_loading_layout))
        else -> ViewHolder(parent.inflate(R.layout.lead_item_layout))
    }

    override fun getItemCount(): Int = leadList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lead = leadList[position]
        holder.bind(lead)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == leadList.size - 1) TYPE_LOADING else TYPE_RECIPE
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun swap(leads: MutableList<Lead>) {
        leadList = ArrayList(leads)
        notifyDataSetChanged()
    }

    fun addItems(leadList: MutableList<Lead>) {
        this.leadList.addAll(leadList)
        notifyItemInserted(this.leadList.size - 1)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(lead: Lead) {

            RxView.clicks(itemView)
                    .map { lead }
                    .subscribe(itemClicked)

            if (lead.socialData != null) {
                lead.socialData?.let {
                    Picasso.get().load(it.photo).into(itemView.leadUserImageView, object : Callback {
                        override fun onSuccess() {
                        }

                        override fun onError(e: Exception) {
                            displayDefaultImage(lead)
                        }

                    })
                }
            } else {
                displayDefaultImage(lead)
            }

            itemView.leadUserNameTextView.text = lead.firstLastName
            itemView.leadTimeTextView.text = lead.createdAt.let { DateHelper.formatDate(it) }
            itemView.leadContentTextView.text = lead.question
        }

        private fun displayDefaultImage(lead: Lead) {
            val title = when {
                !lead.firstLastName.isEmpty() -> lead.firstLastName
                !lead.phone.isEmpty() -> lead.phone.substring(1)
                else -> lead.email
            }

            val result = if (title.isNotEmpty()) title[0].toUpperCase().toString() else ""

            val textDrawable = TextDrawable.builder()
                    .beginConfig()
                    .width(60)
                    .height(60)
                    .endConfig()
                    .buildRect(result, title.hashCode().intToRGB())
            itemView.leadUserImageView.setImageDrawable(textDrawable)
        }
    }
}