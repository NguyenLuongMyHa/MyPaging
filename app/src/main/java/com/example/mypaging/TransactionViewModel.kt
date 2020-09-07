package com.example.mypaging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.android.codelabs.paging.db.TransactionDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionViewModel(private val database: TransactionDatabase, private val myTransactions: ArrayList<Transaction>) : ViewModel() {

//    val examplePagingFlow = Pager(PagingConfig(10)) {
//        TransactionPagingSource(transactions)
//    }.flow.cachedIn(viewModelScope)
    private val UiModel.TransactionItem.categoryIndex: Int
    get() = this.transaction.id / 100

    fun getPagingFlow(): Flow<PagingData<Transaction>> {
//        return Pager(
//            config = PagingConfig(pageSize = 10, enablePlaceholders = false),pagingSourceFactory = { TransactionPagingSource(transactions)}
//        ).flow
        val pagingSourceFactory = { database.transDao().getTrans() }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            remoteMediator = TransactionRemoteMediator(
                database,
                myTransactions
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    val newResult: Flow<PagingData<UiModel>> = getPagingFlow()
        .map { pagingData -> pagingData.map { UiModel.TransactionItem(it) } }
        .map {
            it.insertSeparators<UiModel.TransactionItem, UiModel> { before, after ->
                if (after == null) {
                    // we're at the end of the list
                    return@insertSeparators null
                }

                if (before == null) {
                    // we're at the beginning of the list
                    return@insertSeparators UiModel.SeparatorItem("${after.categoryIndex}00+ category")
                }
                // check between 2 items
                if (before.categoryIndex < after.categoryIndex) {
                    if (after.categoryIndex >= 1) {
                        UiModel.SeparatorItem("${after.categoryIndex}00+ category")
                    } else {
                        UiModel.SeparatorItem(">> category")
                    }
                } else {
                    // no separator
                    null
                }
            }
        }
        .cachedIn(viewModelScope)

    sealed class UiModel {
        data class TransactionItem(val transaction: Transaction) : UiModel()
        data class SeparatorItem(val description: String) : UiModel()
    }
}
