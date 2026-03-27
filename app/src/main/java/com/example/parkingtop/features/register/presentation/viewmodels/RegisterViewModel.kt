package com.example.parkingtop.features.register.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.features.register.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class RegisterState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val role: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String? = null,
        role: String = "customer",
        profileImage: File? = null
    ) {
        viewModelScope.launch {
            _state.value = RegisterState(isLoading = true)

            val result = registerUseCase(
                email = email,
                password = password,
                fullName = fullName,
                phone = phone,
                role = role,
                profileImage = profileImage
            )

            result.fold(
                onSuccess = { authResult ->
                    tokenDataStore.saveTokens(authResult.token, authResult.refreshToken)

                    _state.value = RegisterState(
                        isSuccess = true,
                        role = authResult.user.role
                    )
                },
                onFailure = { exception ->
                    _state.value = RegisterState(
                        error = exception.message ?: "Error al registrar"
                    )
                }
            )
        }
    }
    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }
}
