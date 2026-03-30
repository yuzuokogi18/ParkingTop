package com.example.parkingtop.features.cliente.BusquedaClient.di

import com.example.parkingtop.features.cliente.BusquedaClient.data.repositories.ParkingRepositoryImpl
import com.example.parkingtop.features.cliente.BusquedaClient.domain.repositories.ParkingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindParkingRepository(
        impl: ParkingRepositoryImpl
    ): ParkingRepository
}