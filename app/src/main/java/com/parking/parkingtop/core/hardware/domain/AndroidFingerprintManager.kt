package com.parking.parkingtop.core.hardware.domain

import android.content.Context
import androidx.biometric.BiometricManager
import com.parking.parkingtop.core.hardware.data.FingerprintManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidFingerprintManager @Inject constructor(
    @ApplicationContext private val context: Context
) : FingerprintManager {

    private val biometricManager = BiometricManager.from(context)

    override fun isHardwareAvailable(): Boolean {
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) != BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
    }

    override fun hasFingerprintsRegistered(): Boolean {
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) != BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
    }

    override fun canAuthenticate(): Boolean {
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }
}