package com.example.mypaging

import androidx.paging.*
import androidx.room.withTransaction
import com.example.android.codelabs.paging.db.RemoteKeys
import com.example.android.codelabs.paging.db.TransactionDatabase
import java.io.InvalidObjectException
import java.lang.Exception

private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class TransactionRemoteMediator(
        private val transactionDatabase: TransactionDatabase,
        private val myTransactions: ArrayList<Transaction>
) : RemoteMediator<Int, Transaction>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Transaction>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                if (remoteKeys == null) {
                    // The LoadType is PREPEND so some data was loaded before,
                    // so we should have been able to get remote keys
                    // If the remoteKeys are null, then we're an invalid state and we have a bug
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
            val endOfPaginationReached = myTransactions.isEmpty()
            transactionDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    transactionDatabase.remoteKeysDao().clearRemoteKeys()
                    transactionDatabase.transDao().clearRepos()
                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
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
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { tran ->
                    // Get the remote keys of the last item retrieved
                    transactionDatabase.remoteKeysDao().remoteKeysTranId(tran.id)
                }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Transaction>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { tran ->
                    // Get the remote keys of the first items retrieved
                    transactionDatabase.remoteKeysDao().remoteKeysTranId(tran.id)
                }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
            state: PagingState<Int, Transaction>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { tranId ->
                transactionDatabase.remoteKeysDao().remoteKeysTranId(tranId)
            }
        }
    }

}