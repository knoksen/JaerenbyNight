package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_routes")
data class SavedRouteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val origin: String,
    val destination: String,
    val modeType: String,
    val safetyScore: Int,
    val durationMinutes: Int,
    val costEstimate: Double,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
