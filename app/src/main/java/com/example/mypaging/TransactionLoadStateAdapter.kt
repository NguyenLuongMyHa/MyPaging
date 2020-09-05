package com.example.mypaging

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class TransactionLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<TransactionLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: TransactionLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TransactionLoadStateViewHolder {
        return TransactionLoadStateViewHolder.create(parent, retry)
    }
}