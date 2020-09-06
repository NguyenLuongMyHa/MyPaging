package com.example.mypaging

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rec_item_transaction.view.*

class TransactionDataAdapter : PagingDataAdapter<Transaction, TransactionDataAdapter.ViewHolder>(DataDifferntiator) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.txt_transaction_name.text = getItem(position)?.name
        holder.itemView.txt_amount.text = getItem(position)?.amount.toString()
        if (getItem(position)?.amount!! < 0)
            holder.itemView.txt_amount.setTextColor(Color.RED)
        else
            holder.itemView.txt_amount.setTextColor(Color.GREEN)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.rec_item_transaction, parent, false)
        )
    }

    object DataDifferntiator : DiffUtil.ItemCallback<Transaction>() {

        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }

}
