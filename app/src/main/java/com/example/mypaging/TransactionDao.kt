package com.example.android.codelabs.paging.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.mypaging.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>)

    @Query("SELECT * FROM trans")
    fun getTrans(): PagingSource<Int, com.example.mypaging.Transaction>

    @Query("DELETE FROM trans")
    suspend fun clearTrans()

}