package com.example.parkingtop.features.propetario.horariopropetario.di

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.horariopropetario.data.repositories.AvailabilityRepositoryImpl
import com.example.parkingtop.features.propetario.horariopropetario.domain.repositories.AvailabilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AvailabilityModule {

    @Provides
    @Singleton
    fun provideAvailabilityRepository(
        api: ParkingApi,
        tokenDataStore: TokenDataStore
    ): AvailabilityRepository {
        return AvailabilityRepositoryImpl(api, tokenDataStore)
    }
}
