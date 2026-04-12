package com.parking.parkingtop.features.register.di

import com.parking.parkingtop.features.register.data.repositories.RegisterRepositoryImpl
import com.parking.parkingtop.features.register.domain.repositories.RegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RegisterModule {

    @Binds
    @Singleton
    abstract fun bindRegisterRepository(
        registerRepositoryImpl: RegisterRepositoryImpl
    ): RegisterRepository
}
