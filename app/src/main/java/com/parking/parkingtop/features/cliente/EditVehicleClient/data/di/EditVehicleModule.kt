package com.parking.parkingtop.features.cliente.EditVehicleClient.data.di


import com.parking.parkingtop.features.cliente.EditVehicleClient.data.repositories.EditVehicleRepositoryImpl
import com.parking.parkingtop.features.cliente.EditVehicleClient.domain.repositories.EditVehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EditVehicleModule {
    @Singleton
    @Binds
    abstract fun bindEditVehicleRepository(
        impl: EditVehicleRepositoryImpl
    ): EditVehicleRepository
}