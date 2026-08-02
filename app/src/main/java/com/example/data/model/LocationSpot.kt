package com.example.data.model

data class LocationSpot(
    val id: String,
    val name: String,
    val category: String, // "Nightlife District", "Transit Hub", "Residential Area", "University Campus", "Entertainment Venue"
    val address: String,
    val isSafetyKioskNearby: Boolean = true,
    val latitude: Double,
    val longitude: Double
)
