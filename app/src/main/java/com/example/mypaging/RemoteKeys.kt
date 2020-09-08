package com.example.android.codelabs.paging.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val transId: Int,
    @ColumnInfo(name = "prevKey") val prevKey: Int?,
    @ColumnInfo(name = "nextKey") val nextKey: Int?
)