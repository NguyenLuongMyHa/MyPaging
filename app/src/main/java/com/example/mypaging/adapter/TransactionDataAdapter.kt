package com.example.mypaging.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypaging.R
import kotlinx.android.synthetic.main.rec_item_transaction.view.*
import com.example.mypaging.ui.TransactionViewModel.UiModel
import com.example.mypaging.model.Transaction

class TransactionDataAdapter : PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(
    UIMODEL_COMPARATOR
) {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(transaction: Transaction) {
            itemView.txt_transaction_name.text = transaction?.name
            itemView.txt_amount.text = transaction?.amount.toString()
            if (transaction?.amount!! < 0)
                itemView.txt_amount.setTextColor(Color.RED)
            else
                itemView.txt_amount.setTextColor(Color.GREEN)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.TransactionItem -> (holder as TransactionViewHolder).bind(uiModel.transaction)
                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == R.layout.rec_item_transaction) {
            return TransactionViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.rec_item_transaction, parent, false)
            )
        } else {
            return SeparatorViewHolder(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.separator_view_item, parent, false)
            )
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.TransactionItem -> R.layout.rec_item_transaction
            is UiModel.SeparatorItem -> R.layout.separator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }
    companion object{
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.TransactionItem && newItem is UiModel.TransactionItem &&
                        oldItem.transaction.id == newItem.transaction.id) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}
