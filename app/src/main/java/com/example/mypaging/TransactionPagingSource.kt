package com.example.mypaging

import androidx.paging.PagingSource
import java.io.IOException


private const val GITHUB_STARTING_PAGE_INDEX = 1

class TransactionPagingSource (private val transactions: List<Transaction>): PagingSource<Int, Transaction>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Transaction> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            LoadResult.Page(
                data = transactions,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (transactions.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        }
    }
}