package com.example.parkingtop.features.propetario.misespaciospropetario.di

import com.example.parkingtop.features.propetario.misespaciospropetario.data.repositories.ParkingSpotRepositoryImpl
import com.example.parkingtop.features.propetario.misespaciospropetario.domain.repositories.ParkingSpotRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MySpacesModule {

    @Binds
    @Singleton
    abstract fun bindParkingSpotRepository(
        parkingSpotRepositoryImpl: ParkingSpotRepositoryImpl
    ): ParkingSpotRepository
}
