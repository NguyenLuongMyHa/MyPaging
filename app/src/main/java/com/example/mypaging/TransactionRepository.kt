package com.example.mypaging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

@ExperimentalCoroutinesApi
class TransactionRepository() {
    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
    fun getSearchResultStream(): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),pagingSourceFactory = { TransactionPagingSource(initTransaction())}
        ).flow
    }
    private fun initTransaction(): ArrayList<Transaction> {
        var transactionList = ArrayList<Transaction>()
        for (i in 1..200) {
            var transaction = Transaction(randomName(), randomAmount())
            transactionList.add(transaction)
        }
        return transactionList
    }
    private fun randomName(): String {
        val list = listOf("Category 1","Category 2","Category 3","Category 4","Category 5","Category 6","Category 7","Category 8","Category 9")
        return list.random()
    }
    private fun randomAmount(): Int{
        return Random.nextInt(-500000, 500000)
    }
}