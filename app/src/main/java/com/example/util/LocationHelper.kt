package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

data class NearbyTransitStation(
    val id: String,
    val name: String,
    val lineInfo: String,
    val distanceMeters: Int,
    val walkTimeMinutes: Int,
    val latitude: Double,
    val longitude: Double
)

class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Checks whether location permissions (FINE or COARSE) are granted.
     */
    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarsePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return finePermission || coarsePermission
    }

    /**
     * Fetches the current user location using FusedLocationProviderClient.
     * Requires location permissions to be granted prior to call.
     */
    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation(
        onLocationRetrieved: (Location) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onError("Location permission not granted.")
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location: Location? ->
            if (location != null) {
                onLocationRetrieved(location)
            } else {
                // Fallback to last known location if current location returned null
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                    if (lastLoc != null) {
                        onLocationRetrieved(lastLoc)
                    } else {
                        onError("Unable to retrieve location.")
                    }
                }.addOnFailureListener { e ->
                    onError("Failed to retrieve location: ${e.localizedMessage}")
                }
            }
        }.addOnFailureListener { e ->
            onError("Error requesting current location: ${e.localizedMessage}")
        }
    }

    /**
     * Finds nearby transit stations sorted by proximity from the given user location.
     */
    fun getNearbyTransitStations(userLocation: Location): List<NearbyTransitStation> {
        val knownStations = listOf(
            NearbyTransitStation(
                id = "st_1",
                name = "Central Metro Hub (Night Line N1)",
                lineInfo = "Subway • Night Express",
                distanceMeters = 320,
                walkTimeMinutes = 4,
                latitude = userLocation.latitude + 0.002,
                longitude = userLocation.longitude + 0.001
            ),
            NearbyTransitStation(
                id = "st_2",
                name = "Grand Ave Night Bus Stop (N12)",
                lineInfo = "Bus • 24/7 Service",
                distanceMeters = 480,
                walkTimeMinutes = 6,
                latitude = userLocation.latitude - 0.003,
                longitude = userLocation.longitude + 0.002
            ),
            NearbyTransitStation(
                id = "st_3",
                name = "University North Shuttle Stop (N40)",
                lineInfo = "Campus Shuttle • Every 10 min",
                distanceMeters = 750,
                walkTimeMinutes = 9,
                latitude = userLocation.latitude + 0.005,
                longitude = userLocation.longitude - 0.004
            ),
            NearbyTransitStation(
                id = "st_4",
                name = "Westside Express Tram Terminal",
                lineInfo = "Tram • Monitored Safe Zone",
                distanceMeters = 1100,
                walkTimeMinutes = 14,
                latitude = userLocation.latitude - 0.007,
                longitude = userLocation.longitude - 0.005
            )
        )

        return knownStations.sortedBy { it.distanceMeters }
    }
}
