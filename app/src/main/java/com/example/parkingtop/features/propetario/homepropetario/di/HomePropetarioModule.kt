package com.example.parkingtop.features.propetario.homepropetario.di

import com.example.parkingtop.features.propetario.homepropetario.data.datasources.HomePropetarioApi
import com.example.parkingtop.features.propetario.homepropetario.data.repositories.HomePropetarioRepositoryImpl
import com.example.parkingtop.features.propetario.homepropetario.domain.repositories.HomePropetarioRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomePropetarioModule {

    @Provides
    @Singleton
    fun provideHomePropetarioApi(retrofit: Retrofit): HomePropetarioApi {
        return retrofit.create(HomePropetarioApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomePropetarioRepository(api: HomePropetarioApi): HomePropetarioRepository {
        return HomePropetarioRepositoryImpl(api)
    }
}
