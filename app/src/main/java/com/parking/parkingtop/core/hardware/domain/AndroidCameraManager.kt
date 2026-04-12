package com.parking.parkingtop.core.hardware.domain

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import com.parking.parkingtop.core.hardware.data.CameraManager
import android.hardware.camera2.CameraManager as AndroidCameraManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidCameraManager @Inject constructor(
    @ApplicationContext private val context: Context
) : CameraManager {

    private val systemCameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as AndroidCameraManager

    override fun hasCamera(): Boolean =
        context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    override fun hasFrontCamera(): Boolean {
        return try {
            systemCameraManager.cameraIdList.any { id ->
                val characteristics = systemCameraManager.getCameraCharacteristics(id)
                characteristics.get(CameraCharacteristics.LENS_FACING) ==
                        CameraCharacteristics.LENS_FACING_FRONT
            }
        } catch (e: Exception) { false }
    }
}