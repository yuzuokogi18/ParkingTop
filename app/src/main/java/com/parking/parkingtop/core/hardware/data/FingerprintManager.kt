package com.parking.parkingtop.core.hardware.data

interface FingerprintManager {

    fun isHardwareAvailable(): Boolean

    fun hasFingerprintsRegistered(): Boolean

    fun canAuthenticate(): Boolean
}