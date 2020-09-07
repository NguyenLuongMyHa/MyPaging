package com.example.mypaging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.transaction_load_state_footer_view_item.view.*
import kotlin.random.Random

class TransactionLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<TransactionLoadStateAdapter.TransactionLoadStateViewHolder>() {
    override fun onBindViewHolder(
        holder: TransactionLoadStateAdapter.TransactionLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): TransactionLoadStateAdapter.TransactionLoadStateViewHolder {
        return TransactionLoadStateViewHolder.create(parent, retry)
    }

    class TransactionLoadStateViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
        init {
            itemView.retry_button.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                itemView.error_msg.text = loadState.error.localizedMessage
            }
            itemView.progress_bar.isVisible = loadState is LoadState.Loading
            itemView.retry_button.isVisible = loadState !is LoadState.Loading
            itemView.error_msg.isVisible = loadState !is LoadState.Loading
        }


        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): TransactionLoadStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.transaction_load_state_footer_view_item, parent, false)
                return TransactionLoadStateViewHolder(view, retry)
            }
        }
    }
}