package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_transit_schedules")
data class CachedTransitScheduleEntity(
    @PrimaryKey val stationId: String,
    val stationName: String,
    val lineCode: String,
    val nextDeparturesJson: String,
    val cachedAtTimestamp: Long = System.currentTimeMillis(),
    val isOfflineAvailable: Boolean = true
)
