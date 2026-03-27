package com.example.parkingtop.features.register.presentation.viewmodels

import android.net.Uri
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
    val selectedImageUri: Uri? = null,
    val selectedImageFile: File? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val tokenDataStore: TokenDataStore
) : ViewModel() {

    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    fun onImageSelected(uri: Uri?, file: File?) {
        _state.value = _state.value.copy(selectedImageUri = uri, selectedImageFile = file)
    }

    fun register(
        email: String,
        password: String,
        fullName: String,
        phone: String? = null,
        role: String = "customer"
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            
            val result = registerUseCase(
                email = email,
                password = password,
                fullName = fullName,
                phone = phone,
                role = role,
                profileImage = _state.value.selectedImageFile
            )

            result.fold(
                onSuccess = { authResult ->
                    tokenDataStore.saveTokens(authResult.token, authResult.refreshToken ?: "")
                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                },
                onFailure = { exception ->
                    _state.value = _state.value.copy(isLoading = false, error = exception.message ?: "Error al registrar")
                }
            )
        }
    }
    
    fun resetError() {
        _state.value = _state.value.copy(error = null)
    }
}
