package com.parking.parkingtop.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.parking.parkingtop.core.database.entities.UserLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLocationDao {

    // Guardar última ubicación del usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLocation(location: UserLocationEntity)

    // Obtener última ubicación
    @Query("SELECT * FROM user_location ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLocation(): UserLocationEntity?

    // Historial de ubicaciones
    @Query("SELECT * FROM user_location ORDER BY timestamp DESC LIMIT :limit")
    fun getLocationHistory(limit: Int = 10): Flow<List<UserLocationEntity>>
}