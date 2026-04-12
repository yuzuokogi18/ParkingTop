package com.parking.parkingtop.core.hardware.data


interface CameraManager {
    fun hasCamera(): Boolean
    fun hasFrontCamera(): Boolean
}