package com.example.mypaging.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mypaging.db.TransactionDatabase
import com.example.mypaging.model.Transaction
import com.example.mypaging.ui.TransactionViewModel

class TransactionViewModelFactory (var transactions: ArrayList<Transaction>,
                                   var transactionDatabase: TransactionDatabase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionDatabase, transactions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}