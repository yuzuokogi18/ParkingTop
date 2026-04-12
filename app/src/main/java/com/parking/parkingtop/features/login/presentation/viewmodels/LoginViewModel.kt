package com.parking.parkingtop.features.login.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.hardware.data.FingerprintManager
import com.parking.parkingtop.features.login.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val role: String? = null,
    val biometricAvailable: Boolean = false,
    val showBiometricPrompt: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenDataStore: TokenDataStore,
    private val fingerprintManager: FingerprintManager
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    init {
        // Verifica si el dispositivo soporta biometría
        _state.value = _state.value.copy(
            biometricAvailable = fingerprintManager.canAuthenticate()
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {

            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { authResult ->

                    tokenDataStore.saveTokens(
                        authResult.token,
                        authResult.refreshToken ?: ""
                    )

                    _state.value = _state.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        role = authResult.user.role
                    )
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    // ===============================
    // BIOMETRIC LOGIN
    // ===============================

    fun requestBiometricLogin() {

        if (!fingerprintManager.canAuthenticate()) {

            if (!fingerprintManager.isHardwareAvailable()) {
                _state.value = _state.value.copy(
                    error = "Este dispositivo no tiene sensor de huella"
                )
            }

            else if (!fingerprintManager.hasFingerprintsRegistered()) {
                _state.value = _state.value.copy(
                    error = "No hay huellas registradas. Ve a Configuración > Seguridad"
                )
            }

            return
        }

        _state.value = _state.value.copy(
            showBiometricPrompt = true
        )
    }

    fun onBiometricSuccess() {

        _state.value = _state.value.copy(
            showBiometricPrompt = false
        )

        viewModelScope.launch {

            val token = tokenDataStore.accessToken.first()

            if (token != null) {

                _state.value = _state.value.copy(
                    isSuccess = true,
                    role = "customer"
                )

            } else {

                _state.value = _state.value.copy(
                    error = "No hay sesión guardada. Inicia sesión primero."
                )

            }
        }
    }

    fun onBiometricError(message: String) {

        _state.value = _state.value.copy(
            showBiometricPrompt = false,
            error = message
        )
    }

    fun dismissBiometricPrompt() {
        _state.value = _state.value.copy(
            showBiometricPrompt = false
        )
    }

    fun resetError() {
        _state.value = _state.value.copy(
            error = null
        )
    }
}