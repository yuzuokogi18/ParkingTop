package com.example.parkingtop.features.cliente.updateProfile.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.cliente.updateProfile.domain.usecases.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class UpdateProfileUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String?     = null
)

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val useCase: UpdateProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UpdateProfileUiState())
    val state: StateFlow<UpdateProfileUiState> = _state

    fun updateProfile(
        fullName: String,
        phone: String?,
        profileImage: File?
    ) {
        viewModelScope.launch {
            _state.value = UpdateProfileUiState(isLoading = true)

            val result = useCase.execute(fullName, phone, profileImage)

            result.onSuccess {
                _state.value = UpdateProfileUiState(isSuccess = true)
            }
            result.onFailure { e ->
                _state.value = UpdateProfileUiState(error = e.message ?: "Error al actualizar perfil")
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}