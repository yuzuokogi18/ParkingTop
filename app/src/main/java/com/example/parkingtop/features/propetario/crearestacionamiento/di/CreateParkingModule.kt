package com.example.parkingtop.features.propetario.crearestacionamiento.di

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.crearestacionamiento.data.repositories.CreateParkingRepositoryImpl
import com.example.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
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
