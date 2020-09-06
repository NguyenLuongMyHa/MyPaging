package com.example.mypaging

import androidx.paging.PagingSource
import java.lang.Exception

class TransactionPagingSource(val transactions : ArrayList<Transaction>) : PagingSource<Int, Transaction>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        try {
            val pageNumber = params.key?:0
            val response = getListData(transactions,pageNumber)
            val responseData = mutableListOf<Transaction>()
            val data = response?: emptyList()
            responseData.addAll(data)

            return LoadResult.Page(
                data = responseData,
                prevKey = if (pageNumber == 1) 0 else pageNumber - 1,
                nextKey = pageNumber + 1
            )
        }
        catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
    private fun getListData(transactions: ArrayList<Transaction>, page: Int) : List<Transaction> {
        return transactions.slice(page*10 until page*10+10)
    }
}