package com.app.venyoo.extension

import android.content.Context
import android.content.Intent
import com.app.venyoo.network.model.Lead
import com.app.venyoo.screens.lead.detail.LeadDetailActivity

fun Context.leadDetail(lead: Lead): Intent = Intent(this, LeadDetailActivity::class.java).apply { putExtra(LeadDetailActivity.LEAD, lead) }