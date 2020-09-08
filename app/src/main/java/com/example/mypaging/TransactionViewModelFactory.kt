package com.example.mypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.codelabs.paging.db.TransactionDatabase

class TransactionViewModelFactory (var transactions: ArrayList<Transaction>,
                                   var transactionDatabase: TransactionDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactionDatabase, transactions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}