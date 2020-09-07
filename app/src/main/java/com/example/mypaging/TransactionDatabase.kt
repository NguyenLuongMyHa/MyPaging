package com.example.android.codelabs.paging.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
        entities = [com.example.mypaging.Transaction::class, RemoteKeys::class],
        version = 1,
        exportSchema = false
)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun transDao(): TransactionDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getInstance(context: Context): TransactionDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    TransactionDatabase::class.java, "my_transaction.db")
                        .build()
    }
}