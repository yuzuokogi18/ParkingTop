package com.parking.parkingtop.features.register.presentation.viewmodels

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.hardware.data.CameraManager
import com.parking.parkingtop.features.register.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class RegisterState(
    val isLoading: Boolean    = false,
    val error: String?        = null,
    val isSuccess: Boolean    = false,
    val role: String?         = null,
    val profileImageUri: Uri? = null,

    // ✅ capacidades de cámara
    val cameraAvailable: Boolean              = false,
    val frontCameraAvailable: Boolean         = false,
    val showCameraPermissionRationale: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val tokenDataStore: TokenDataStore,
    private val cameraManager: CameraManager
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    init {
        _state.value = _state.value.copy(
            cameraAvailable      = cameraManager.hasCamera(),
            frontCameraAvailable = cameraManager.hasFrontCamera()
        )
    }

    fun onCameraPermissionDenied() {
        _state.value = _state.value.copy(showCameraPermissionRationale = true)
    }

    fun dismissCameraRationale() {
        _state.value = _state.value.copy(showCameraPermissionRationale = false)
    }

    fun onProfileImageSelected(uri: Uri) {
        _state.value = _state.value.copy(profileImageUri = uri)
    }

    fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String? = null,
        role: String = "customer",
        profileImage: File? = null
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = registerUseCase(
                email        = email,
                password     = password,
                fullName     = fullName,
                phone        = phone,
                role         = role,
                profileImage = profileImage
            )

            result.fold(
                onSuccess = { authResult ->
                    tokenDataStore.saveTokens(authResult.token, authResult.refreshToken)
                    _state.value = RegisterState(
                        isSuccess       = true,
                        role            = authResult.user.role,
                        profileImageUri = _state.value.profileImageUri
                    )
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error     = exception.message ?: "Error al registrar"
                    )
                }
            )
        }
    }

    fun resetError() { _state.value = _state.value.copy(error = null) }
    fun resetState() { _state.value = RegisterState() }
}