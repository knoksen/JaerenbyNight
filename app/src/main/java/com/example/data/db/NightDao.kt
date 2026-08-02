package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NightDao {
    // Saved Routes
    @Query("SELECT * FROM saved_routes ORDER BY timestamp DESC")
    fun getAllSavedRoutes(): Flow<List<SavedRouteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedRoute(route: SavedRouteEntity)

    @Query("DELETE FROM saved_routes WHERE id = :id")
    suspend fun deleteSavedRouteById(id: Int)

    // Emergency Contacts
    @Query("SELECT * FROM emergency_contacts ORDER BY isPrimary DESC, name ASC")
    fun getAllEmergencyContacts(): Flow<List<EmergencyContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(contact: EmergencyContactEntity)

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteEmergencyContactById(id: Int)

    // Safety Logs
    @Query("SELECT * FROM night_safety_logs ORDER BY timestamp DESC")
    fun getAllSafetyLogs(): Flow<List<NightSafetyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSafetyLog(log: NightSafetyLogEntity)

    // Trip Planner: Frequent Destinations & Preferred Modes
    @Query("SELECT * FROM frequent_destinations ORDER BY timestamp DESC")
    fun getAllFrequentDestinations(): Flow<List<FrequentDestinationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFrequentDestination(destination: FrequentDestinationEntity)

    @Query("DELETE FROM frequent_destinations WHERE id = :id")
    suspend fun deleteFrequentDestinationById(id: Int)

    // Cached Transit Schedules (Offline Connectivity)
    @Query("SELECT * FROM cached_transit_schedules ORDER BY cachedAtTimestamp DESC")
    fun getAllCachedTransitSchedules(): Flow<List<CachedTransitScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedTransitSchedule(schedule: CachedTransitScheduleEntity)

    @Query("DELETE FROM cached_transit_schedules WHERE stationId = :stationId")
    suspend fun deleteCachedTransitSchedule(stationId: String)

    // Cached Map Tiles (Offline Connectivity)
    @Query("SELECT * FROM cached_map_tiles ORDER BY cachedAtTimestamp DESC")
    fun getAllCachedMapTiles(): Flow<List<CachedMapTileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedMapTile(tile: CachedMapTileEntity)

    @Query("DELETE FROM cached_map_tiles WHERE tileKey = :tileKey")
    suspend fun deleteCachedMapTile(tileKey: String)

    @Query("DELETE FROM cached_map_tiles")
    suspend fun clearAllCachedMapTiles()
}
