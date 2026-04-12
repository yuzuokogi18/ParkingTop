package com.parking.parkingtop.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parking.parkingtop.core.database.entities.ParkingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParkingDao {

    // Obtener todos los parkings cacheados
    @Query("SELECT * FROM parkings")
    fun getAllParkings(): Flow<List<ParkingEntity>>

    // Obtener parkings cercanos (Radio en metros)
    @Query("""
        SELECT * FROM parkings 
        WHERE (
            6371000 * acos(
                cos(radians(:userLat)) * 
                cos(radians(latitude)) * 
                cos(radians(longitude) - radians(:userLng)) + 
                sin(radians(:userLat)) * 
                sin(radians(latitude))
            )
        ) <= :radiusMeters
        ORDER BY (
            6371000 * acos(
                cos(radians(:userLat)) * 
                cos(radians(latitude)) * 
                cos(radians(longitude) - radians(:userLng)) + 
                sin(radians(:userLat)) * 
                sin(radians(latitude))
            )
        ) ASC
    """)
    fun getNearbyParkings(
        userLat: Double,
        userLng: Double,
        radiusMeters: Int = 5000
    ): Flow<List<ParkingEntity>>

    // Insertar parkings
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParkings(parkings: List<ParkingEntity>)

    // Limpiar parkings antiguos
    @Query("DELETE FROM parkings WHERE cachedAt < :timestamp")
    suspend fun deleteOldParkings(timestamp: Long)
}