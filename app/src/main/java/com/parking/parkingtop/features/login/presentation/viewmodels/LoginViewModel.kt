package com.parking.parkingtop.features.login.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.di.navigation.DeepLinkHandler
import com.parking.parkingtop.core.hardware.data.FingerprintManager
import com.parking.parkingtop.features.login.domain.usecases.LoginUseCase
import com.parking.parkingtop.features.login.domain.usecases.GetMySubscriptionUseCase
import com.parking.parkingtop.features.notifications.data.repositories.FcmTokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val role: String? = null,
    val hasActiveSubscription: Boolean = false,
    val biometricAvailable: Boolean = false,
    val showBiometricPrompt: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getMySubscriptionUseCase: GetMySubscriptionUseCase,
    private val tokenDataStore: TokenDataStore,
    private val fingerprintManager: FingerprintManager,
    private val fcmTokenRepository: FcmTokenRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state
    @Inject lateinit var deepLinkHandler: DeepLinkHandler

    init {
        _state.value = _state.value.copy(
            biometricAvailable = fingerprintManager.canAuthenticate()
        )
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {

            _state.value = _state.value.copy(isLoading = true, error = null)

            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { authResult ->

                    tokenDataStore.saveTokens(
                        authResult.token,
                        authResult.refreshToken ?: ""
                    )

                    registerFcmToken(authResult.token)

                    // Si es owner, verificamos si ya tiene suscripción activa
                    if (authResult.user.role == "owner") {
                        checkOwnerSubscription(
                            token = authResult.token,
                            role  = authResult.user.role
                        )
                    } else {
                        deepLinkHandler.updateAuthState(loggedIn = true, role = authResult.user.role)
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isSuccess = true,
                            role      = authResult.user.role
                        )
                    }
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error     = exception.message ?: "Error al iniciar sesión"
                    )
                }
            )
        }
    }

    private suspend fun checkOwnerSubscription(token: String, role: String) {
        val subscriptionResult = getMySubscriptionUseCase("Bearer $token")

        subscriptionResult.fold(
            onSuccess = { subscription ->
                val isActive = subscription?.status == "active" ||
                        subscription?.status == "trialing"

                // En onSuccess y onFailure — usar el parámetro `role` que ya tienes
                deepLinkHandler.updateAuthState(loggedIn = true, role = role)
                _state.value = _state.value.copy(
                    isLoading             = false,
                    isSuccess             = true,
                    role                  = role,
                    hasActiveSubscription = isActive
                )
            },
            onFailure = { error ->
                deepLinkHandler.updateAuthState(loggedIn = true, role = role)
                _state.value = _state.value.copy(
                    isLoading             = false,
                    isSuccess             = true,
                    role                  = role,
                    hasActiveSubscription = false
                )
            }
        )
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
            } else if (!fingerprintManager.hasFingerprintsRegistered()) {
                _state.value = _state.value.copy(
                    error = "No hay huellas registradas. Ve a Configuración > Seguridad"
                )
            }
            return
        }

        _state.value = _state.value.copy(showBiometricPrompt = true)
    }

    fun onBiometricSuccess() {
        _state.value = _state.value.copy(showBiometricPrompt = false)

        viewModelScope.launch {
            val token = tokenDataStore.accessToken.first()

            if (token != null) {
                // El biométrico siempre es cliente
                deepLinkHandler.updateAuthState(loggedIn = true, role = "customer")
                _state.value = _state.value.copy(isSuccess = true, role = "customer")
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
            error               = message
        )
    }

    fun dismissBiometricPrompt() {
        _state.value = _state.value.copy(showBiometricPrompt = false)
    }

    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun registerFcmToken(accessToken: String) {
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { fcmToken ->
                android.util.Log.d("FCM_DEBUG", "Registrando token: $fcmToken")
                fcmTokenRepository.registerTokenAsync(fcmToken, accessToken)  // ← pasa el token
            }
            .addOnFailureListener { e ->
                android.util.Log.e("FCM_DEBUG", "Error: ${e.message}")
            }
    }
}