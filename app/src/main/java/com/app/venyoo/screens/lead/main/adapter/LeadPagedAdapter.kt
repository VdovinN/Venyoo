package com.app.venyoo.screens.lead.main.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.app.venyoo.R
import com.app.venyoo.network.NetworkState
import com.app.venyoo.network.model.Lead
import io.reactivex.subjects.PublishSubject

class LeadPagedAdater(private val retryCallback: () -> Unit) : PagedListAdapter<Lead, RecyclerView.ViewHolder>(LeadDiffCallback) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.lead_item_layout -> (holder as LeadViewHolder).bind(getItem(position), itemClicked)
            R.layout.item_network_state -> (holder as NetworkStateViewHolder).bindTo(networkState)
        }
    }

    val itemClicked: PublishSubject<Lead> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.lead_item_layout -> LeadViewHolder.create(parent)
            R.layout.item_network_state -> NetworkStateViewHolder.create(parent, retryCallback)
            else -> throw IllegalStateException("unknown view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.lead_item_layout
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    companion object {
        val LeadDiffCallback = object : DiffUtil.ItemCallback<Lead>() {
            override fun areItemsTheSame(oldItem: Lead, newItem: Lead): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Lead, newItem: Lead): Boolean = oldItem == newItem
        }
    }
}