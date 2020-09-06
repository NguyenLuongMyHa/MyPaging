package com.example.mypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TransactionViewModelFactory (var transactions: ArrayList<Transaction>) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(transactions) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}