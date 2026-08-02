package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SavedRouteEntity::class,
        EmergencyContactEntity::class,
        NightSafetyLogEntity::class,
        FrequentDestinationEntity::class,
        CachedTransitScheduleEntity::class,
        CachedMapTileEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class NightDatabase : RoomDatabase() {
    abstract fun nightDao(): NightDao

    companion object {
        @Volatile
        private var INSTANCE: NightDatabase? = null

        fun getDatabase(context: Context): NightDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NightDatabase::class.java,
                    "night_commute_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
