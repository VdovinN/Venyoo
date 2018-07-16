package com.app.venyoo.screens.lead.detail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.app.venyoo.R
import com.app.venyoo.extension.setAutoscale
import kotlinx.android.synthetic.main.lead_detail_spinner_item_layout.view.*


class LeadDetailSpinnerAdapter(context: Context?, var statusList: List<Pair<String, String>>) : BaseAdapter() {

    private val inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ItemRowHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.lead_detail_spinner_item_layout, parent, false)
            holder = ItemRowHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ItemRowHolder
        }

        holder.bind(statusList[position])

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ItemRowHolder
        if (convertView == null) {
            view = inflater.inflate(R.layout.lead_detail_spinner_dropdown_item_layout, parent, false)
            holder = ItemRowHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ItemRowHolder
        }

        holder.bind(statusList[position])

        return view
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = statusList.size

    private class ItemRowHolder(val row: View) {

        init {
            row.leadUserStatusTextView.setAutoscale()
        }

        fun bind(pair: Pair<String, String>) {

            row.leadUserStatusTextView.text = pair.second

        }

    }

}