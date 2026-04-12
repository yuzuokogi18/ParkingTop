package com.parking.parkingtop.features.cliente.HomeClient.di

import com.parking.parkingtop.features.cliente.HomeClient.data.repositories.HomeRepositoryImpl
import com.parking.parkingtop.features.cliente.HomeClient.domain.repositories.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository
}
