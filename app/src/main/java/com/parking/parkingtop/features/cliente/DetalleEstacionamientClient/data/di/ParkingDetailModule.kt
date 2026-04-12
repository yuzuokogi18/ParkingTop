package com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.di

import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.data.repositories.ParkingDetalleRepositoryImpl
import com.parking.parkingtop.features.cliente.DetalleEstacionamientClient.domain.repositories.ParkingDetalleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ParkingDetailModule {


    @Singleton
    @Binds
    abstract fun bindParkingDetailRepository(
        parkingDetailRepositoryImpl: ParkingDetalleRepositoryImpl
    ): ParkingDetalleRepository
}