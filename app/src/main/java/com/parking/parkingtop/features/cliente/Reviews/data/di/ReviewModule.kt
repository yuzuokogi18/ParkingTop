package com.parking.parkingtop.features.cliente.Reviews.data.di

import com.parking.parkingtop.features.cliente.Reviews.data.repositories.ReviewRepositoryImpl
import com.parking.parkingtop.features.cliente.Reviews.domain.repositories.ReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {
    @Singleton
    @Binds
    abstract fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository
}