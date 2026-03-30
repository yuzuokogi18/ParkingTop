package com.example.parkingtop.features.cliente.Notifications.data.di

import com.example.parkingtop.features.cliente.Notifications.data.repositories.NotificationRepositoryImpl
import com.example.parkingtop.features.cliente.Notifications.domain.repositories.NotificationRepository
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