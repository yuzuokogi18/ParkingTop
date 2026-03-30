package com.example.parkingtop.features.cliente.CreateVehicleClient.data.di

import com.example.parkingtop.features.cliente.CreateVehicleClient.data.repositories.CreateVehicleRepositoryImpl
import com.example.parkingtop.features.cliente.CreateVehicleClient.domain.repositories.CreateVehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CreateVehicleModule {

    @Singleton
    @Binds
    abstract fun bindCreateVehicleRepository(
        impl: CreateVehicleRepositoryImpl
    ): CreateVehicleRepository
}