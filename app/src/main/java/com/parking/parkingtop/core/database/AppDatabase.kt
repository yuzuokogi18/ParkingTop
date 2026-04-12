package com.parking.parkingtop.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.parking.parkingtop.core.database.dao.ParkingDao
import com.parking.parkingtop.core.database.dao.UserLocationDao
import com.parking.parkingtop.core.database.entities.ParkingEntity
import com.parking.parkingtop.core.database.entities.UserLocationEntity

@Database(
    entities = [
        ParkingEntity::class,
        UserLocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun parkingDao(): ParkingDao
    abstract fun userLocationDao(): UserLocationDao
}