package com.example.mypaging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.android.codelabs.paging.db.RemoteKeys
import com.example.android.codelabs.paging.db.TransactionDatabase
import java.io.InvalidObjectException
import java.lang.Exception

private const val GITHUB_STARTING_PAGE_INDEX = 0

@OptIn(ExperimentalPagingApi::class)
class TransactionRemoteMediator(
        private val transactionDatabase: TransactionDatabase,
        private val transList: ArrayList<Transaction>) : RemoteMediator<Int, Transaction>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Transaction>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    throw InvalidObjectException("Remote key and the prevKey should not be null")
                }
                // If the previous key is null, then we can't request more data
                val prevKey = remoteKeys.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKeys.prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys == null || remoteKeys.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }

        }

        try {
            val myTransactions = getListData(transList, page!!)
            val endOfPaginationReached = myTransactions.isEmpty()
            transactionDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    transactionDatabase.remoteKeysDao().clearRemoteKeys()
                    transactionDatabase.transDao().clearTrans()
                }
                val prevKey = if (page == 0) 0 else page - 1
                val nextKey = if (endOfPaginationReached) page else page + 1
                val keys = myTransactions.map {
                    RemoteKeys(transId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                transactionDatabase.remoteKeysDao().insertAll(keys)
                transactionDatabase.transDao().insertAll(myTransactions)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Transaction>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { tran ->
                // Get the remote keys of the last item retrieved
                transactionDatabase.remoteKeysDao().remoteKeysTranId(tran.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Transaction>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { tran ->
                // Get the remote keys of the first items retrieved
                transactionDatabase.remoteKeysDao().remoteKeysTranId(tran.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Transaction>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { tranId ->
                transactionDatabase.remoteKeysDao().remoteKeysTranId(tranId)
            }
        }
    }
    private fun getListData(transactions: ArrayList<Transaction>, page: Int) : List<Transaction> {
        return if(page>9 && page % 10 == 0 ) {
            val newTransactions = TransactionActivity.initTransaction(page*10)
            TransactionActivity.transactions.addAll(newTransactions)
            newTransactions.slice(0 until 10)
        } else {
            transactions.slice((page)*10 until (page)*10+10)
        }
    }
}