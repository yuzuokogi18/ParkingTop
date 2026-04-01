package com.example.parkingtop.features.propetario.reservationpropetario.di

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.reservationpropetario.data.repositories.ReservationOwnerRepositoryImpl
import com.example.parkingtop.features.propetario.reservationpropetario.domain.repositories.ReservationOwnerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReservationOwnerModule {

    @Provides
    @Singleton
    fun provideReservationOwnerRepository(
        api: ParkingApi,
        tokenDataStore: TokenDataStore
    ): ReservationOwnerRepository {
        return ReservationOwnerRepositoryImpl(api, tokenDataStore)
    }
}
