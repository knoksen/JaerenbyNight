package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "frequent_destinations")
data class FrequentDestinationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val address: String,
    val category: String = "Favorite",
    val preferredMode: String = "HYBRID",
    val latitude: Double = 37.7620,
    val longitude: Double = -122.4350,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
