package com.parking.parkingtop.features.notifications.data.di

import com.parking.parkingtop.features.notifications.data.repositories.NotificationRepositoryImpl
import com.parking.parkingtop.features.notifications.domain.repositories.NotificationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        impl: NotificationRepositoryImpl
    ): NotificationRepository
}