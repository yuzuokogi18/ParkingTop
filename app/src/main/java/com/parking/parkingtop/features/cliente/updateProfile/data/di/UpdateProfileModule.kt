package com.parking.parkingtop.features.cliente.updateProfile.data.di

import com.parking.parkingtop.features.cliente.updateProfile.data.repositories.UpdateProfileRepositoryImpl
import com.parking.parkingtop.features.cliente.updateProfile.domain.repositories.UpdateProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UpdateProfileModule {
    @Singleton
    @Binds
    abstract fun bindUpdateProfileRepository(
        impl: UpdateProfileRepositoryImpl
    ): UpdateProfileRepository
}
