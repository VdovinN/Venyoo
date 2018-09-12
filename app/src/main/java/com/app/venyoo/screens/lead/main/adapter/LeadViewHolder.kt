package com.app.venyoo.screens.lead.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.app.venyoo.R
import com.app.venyoo.extension.intToRGB
import com.app.venyoo.helper.DateHelper
import com.app.venyoo.network.model.Lead
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.lead_item_layout.view.*
import java.lang.Exception

class LeadViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        fun create(parent: ViewGroup): LeadViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.lead_item_layout, parent, false)
            return LeadViewHolder(view)
        }
    }

    fun bind(lead: Lead?) {

        /* RxView.clicks(itemView)
                 .map { lead }
                 .subscribe(itemClicked)*/

        lead?.let { nonNullLead ->
            {
                if (nonNullLead.socialData != null) {
                    nonNullLead.socialData?.let {
                        Picasso.get().load(it.photo).into(itemView.leadUserImageView, object : Callback {
                            override fun onSuccess() {
                            }

                            override fun onError(e: Exception) {
                                displayDefaultImage(nonNullLead)
                            }

                        })
                    }
                } else {
                    displayDefaultImage(nonNullLead)
                }

                itemView.leadUserNameTextView.text = nonNullLead.firstLastName
                itemView.leadTimeTextView.text = nonNullLead.createdAt.let { DateHelper.formatDate(it) }
                itemView.leadContentTextView.text = nonNullLead.question
            }
        }
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