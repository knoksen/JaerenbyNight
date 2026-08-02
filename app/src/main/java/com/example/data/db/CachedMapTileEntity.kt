package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_map_tiles")
data class CachedMapTileEntity(
    @PrimaryKey val tileKey: String,
    val regionName: String,
    val zoomLevel: Int,
    val cachedAtTimestamp: Long = System.currentTimeMillis(),
    val dataSizeBytes: Int = 142500,
    val isOfflineReady: Boolean = true
)
