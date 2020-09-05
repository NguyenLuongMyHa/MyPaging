package com.example.mypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
class TransactionRepositoriesViewModel(private val repository: TransactionRepository) : ViewModel() {

    fun searchRepo(): Flow<PagingData<Transaction>> {
        return repository.getSearchResultStream()
            .cachedIn(viewModelScope)
    }
}