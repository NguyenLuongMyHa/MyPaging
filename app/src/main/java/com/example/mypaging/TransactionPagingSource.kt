package com.example.mypaging
import androidx.paging.PagingSource
import java.lang.Exception

class TransactionPagingSource(var transactions : ArrayList<Transaction>) : PagingSource<Int, Transaction>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        try {
            val pageNumber = params.key?:0
            val response = getListData(transactions,pageNumber)
            val responseData = mutableListOf<Transaction>()
            val data = response?: emptyList()
            responseData.addAll(data)

            return LoadResult.Page(
                data = responseData,
                prevKey = null,
                nextKey = pageNumber + 1
            )
        }
        catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun getListData(transactions: ArrayList<Transaction>, page: Int) : List<Transaction> {
        return if(page>9 && page % 10 == 0 ) {
            val newTransactions = TransactionActivity.initTransaction(page*10)
            TransactionActivity.transactions.addAll(newTransactions)
            newTransactions.slice(0 until 10)
        } else {
            transactions.slice(page*10 until page*10+10)
        }
    }
}