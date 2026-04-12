package com.parking.parkingtop.features.cliente.ReservaClient.data.di

import com.parking.parkingtop.features.cliente.ReservaClient.data.repositories.ReservationRepositoryImpl
import com.parking.parkingtop.features.cliente.ReservaClient.domain.repositories.ReservationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReservationModule {
    @Singleton
    @Binds
    abstract fun bindReservationRepository(
        impl: ReservationRepositoryImpl
    ): ReservationRepository
}
