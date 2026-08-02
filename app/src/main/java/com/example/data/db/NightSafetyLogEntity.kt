package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "night_safety_logs")
data class NightSafetyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val routeTitle: String,
    val commuteTimeMinutes: Int,
    val safetyRatingStars: Int,
    val userFeedback: String,
    val timestamp: Long = System.currentTimeMillis()
)
