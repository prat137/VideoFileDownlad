package com.example.demoapp.ui.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapp.R
import com.example.demoapp.utils.paging.State


class NetworkStateViewHolder(val view: View, val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

    private val progressBar = view.findViewById<ProgressBar>(R.id.loadingProgressBar)
    private val retry = view.findViewById<Button>(R.id.retryLoadingButton)
    private val errorMsg = view.findViewById<TextView>(R.id.errorMessageTextView)

    init {
        retry.setOnClickListener {retryCallback()}
    }

    fun bindTo(networkState: State?) {
/*        progressBar.visibility = toVisibility(networkState == Status.RUNNING)
        retry.visibility = toVisibility(networkState == Status.FAILED)*/
        errorMsg.visibility = toVisibility(networkState != null)
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            return NetworkStateViewHolder(view, retryCallback)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}