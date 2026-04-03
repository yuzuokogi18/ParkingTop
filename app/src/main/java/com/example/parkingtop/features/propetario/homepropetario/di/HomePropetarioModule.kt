package com.example.parkingtop.features.propetario.homepropetario.di

import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.features.propetario.homepropetario.data.repositories.HomePropetarioRepositoryImpl
import com.example.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
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
