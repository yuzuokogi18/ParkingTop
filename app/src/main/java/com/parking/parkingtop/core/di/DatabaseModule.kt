package com.parking.parkingtop.core.di

import android.content.Context
import androidx.room.Room
import com.parking.parkingtop.core.database.AppDatabase
import com.parking.parkingtop.core.database.dao.ParkingDao
import com.parking.parkingtop.core.database.dao.UserLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ParkingTopDB"
        ).build()
    }

    @Provides
    fun provideParkingDao(db: AppDatabase): ParkingDao = db.parkingDao()

    @Provides
    fun provideUserLocationDao(db: AppDatabase): UserLocationDao = db.userLocationDao()
}