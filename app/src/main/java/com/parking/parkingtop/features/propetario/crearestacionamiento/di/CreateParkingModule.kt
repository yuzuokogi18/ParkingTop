package com.parking.parkingtop.features.propetario.crearestacionamiento.di

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.crearestacionamiento.data.repositories.CreateParkingRepositoryImpl
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CreateParkingModule {

    @Provides
    @Singleton
    fun provideCreateParkingRepository(
        api: ParkingApi,
        tokenDataStore: TokenDataStore
    ): CreateParkingRepository {
        return CreateParkingRepositoryImpl(api, tokenDataStore)
    }
}
