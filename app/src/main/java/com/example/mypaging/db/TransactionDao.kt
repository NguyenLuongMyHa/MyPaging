package com.example.mypaging.db

import androidx.paging.PagingSource
import androidx.room.*
import com.example.mypaging.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<Transaction>)

    @Query("SELECT * FROM trans")
    fun getTrans(): PagingSource<Int, Transaction>

    @Query("DELETE FROM trans")
    suspend fun clearTrans()

}