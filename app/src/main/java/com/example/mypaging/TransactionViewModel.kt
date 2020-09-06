package com.example.mypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn

class TransactionViewModel(var transactions: ArrayList<Transaction>) : ViewModel() {

    val examplePagingFlow = Pager(PagingConfig(10)) {
        TransactionPagingSource(transactions)
    }.flow.cachedIn(viewModelScope)
}
