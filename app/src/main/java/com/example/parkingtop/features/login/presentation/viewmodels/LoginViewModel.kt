package com.example.parkingtop.features.login.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.core.datastore.TokenDataStore
import com.example.parkingtop.features.login.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val userRole: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState(isLoading = true)
            
            val result = loginUseCase(email, password)

            result.fold(
                onSuccess = { authResult ->
                    tokenDataStore.saveTokens(authResult.token, authResult.refreshToken ?: "")
                    _state.value = LoginState(isSuccess = true, userRole = authResult.user.role)
                },
                onFailure = { exception ->
                    _state.value = LoginState(error = exception.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }
}
