package com.parking.parkingtop.features.propetario.homepropetario.di

import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.features.propetario.homepropetario.data.repositories.HomePropetarioRepositoryImpl
import com.parking.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomePropetarioModule {

    @Provides
    @Singleton
    fun provideHomePropetarioRepository(
        api: ParkingApi,
        tokenDataStore: TokenDataStore
    ): HomePropetarioRepository {
        return HomePropetarioRepositoryImpl(api, tokenDataStore)
    }
}
