package com.parking.parkingtop.features.propetario.homepropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.GeneralSummary
import com.parking.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.parking.parkingtop.features.propetario.homepropetario.domain.usecases.GetHomeDataUseCase
import com.parking.parkingtop.features.propetario.crearestacionamiento.domain.repositories.CreateParkingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomePropetarioState(
    val isLoading: Boolean = false,
    val data: HomePropetarioData? = null,
    val error: String? = null
)

@HiltViewModel
class HomePropetarioViewModel @Inject constructor(
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val parkingRepository: CreateParkingRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomePropetarioState())
    val state: State<HomePropetarioState> = _state

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            getHomeDataUseCase().fold(
                onSuccess = { data ->
                    _state.value = _state.value.copy(isLoading = false, data = data)
                },
                onFailure = { error ->
                    val errorMessage = error.message ?: ""
                    if (errorMessage.contains("404") || errorMessage.contains("not found", ignoreCase = true)) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            data = HomePropetarioData(
                                ownerName = "Bienvenido",
                                parkings = emptyList(),
                                summary = GeneralSummary(0, 0.0),
                                notifications = emptyList()
                            ),
                            error = null
                        )
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar datos"
                        )
                    }
                }
            )
        }
    }

    fun deleteParking(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            parkingRepository.deleteParking(id).fold(
                onSuccess = {
                    loadHomeData()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al eliminar"
                    )
                }
            )
        }
    }
}
