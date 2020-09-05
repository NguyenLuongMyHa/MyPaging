package com.example.mypaging

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rec_item_transaction.view.*

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(transaction: Transaction?) {
        if (transaction == null) {
            itemView.txt_transaction_name.text = ""
            itemView.txt_amount.text = ""

        } else {
            itemView.txt_transaction_name.text = transaction.name
            itemView.txt_amount.text = transaction.amount.toString()
        }
    }

    companion object {
        fun create(parent: ViewGroup): TransactionViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.rec_item_transaction, parent, false)
            return TransactionViewHolder(view)
        }
    }
}
