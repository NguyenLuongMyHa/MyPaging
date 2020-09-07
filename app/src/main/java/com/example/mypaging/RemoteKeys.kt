package com.example.android.codelabs.paging.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val transId: Int,
    val prevKey: Int?,
    var nextKey: Int?
)