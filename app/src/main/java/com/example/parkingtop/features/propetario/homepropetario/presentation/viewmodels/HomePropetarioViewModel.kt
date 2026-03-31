package com.example.parkingtop.features.propetario.homepropetario.presentation.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.propetario.homepropetario.domain.entities.HomePropetarioData
import com.example.parkingtop.features.propetario.homepropetario.domain.usecases.GetHomeDataUseCase
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
    private val getHomeDataUseCase: GetHomeDataUseCase
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
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconocido"
                    )
                }
            )
        }
    }
}
