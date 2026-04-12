package com.parking.parkingtop.core.di

import com.parking.parkingtop.core.hardware.data.CameraManager
import com.parking.parkingtop.core.hardware.data.FingerprintManager
import com.parking.parkingtop.core.hardware.data.GPSManager
import com.parking.parkingtop.core.hardware.domain.AndroidCameraManager
import com.parking.parkingtop.core.hardware.domain.AndroidFingerprintManager
import com.parking.parkingtop.core.hardware.domain.AndroidGPSManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HardwareModule {

    @Singleton
    @Binds
    abstract fun bindFingerprintManager(
        impl: AndroidFingerprintManager
    ): FingerprintManager

    @Singleton
    @Binds
    abstract fun bindCameraManager(
        impl: AndroidCameraManager
    ): CameraManager

    @Singleton
    @Binds
    abstract fun bindGPSManager(
        impl: AndroidGPSManager
    ): GPSManager
}