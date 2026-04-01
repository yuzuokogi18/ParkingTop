package com.example.parkingtop.features.propetario.horariopropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.propetario.horariopropetario.domain.entities.AvailabilityData
import com.example.parkingtop.features.propetario.horariopropetario.domain.usecases.GetAvailabilityUseCase
import com.example.parkingtop.features.propetario.horariopropetario.domain.usecases.UpdatePublishStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AvailabilityState(
    val isLoading: Boolean = false,
    val data: AvailabilityData? = null,
    val error: String? = null
)

@HiltViewModel
class AvailabilityViewModel @Inject constructor(
    private val getAvailabilityUseCase: GetAvailabilityUseCase,
    private val updatePublishStatusUseCase: UpdatePublishStatusUseCase
) : ViewModel() {

    private val _state = mutableStateOf(AvailabilityState())
    val state: State<AvailabilityState> = _state

    init {
        loadAvailability()
    }

    fun loadAvailability() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getAvailabilityUseCase().fold(
                onSuccess = { data ->
                    _state.value = _state.value.copy(isLoading = false, data = data)
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar disponibilidad"
                    )
                }
            )
        }
    }

    fun togglePublishStatus(isPublished: Boolean) {
        viewModelScope.launch {
            // Optimistic update
            val currentData = _state.value.data
            if (currentData != null) {
                _state.value = _state.value.copy(data = currentData.copy(isPublished = isPublished))
            }

            updatePublishStatusUseCase(isPublished).onFailure { error ->
                // Rollback if failed
                _state.value = _state.value.copy(
                    data = currentData,
                    error = "Error al actualizar estado: ${error.message}"
                )
            }
        }
    }
}
