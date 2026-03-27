package com.example.parkingtop.features.cliente.HomeClient.presentation.screens

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.features.cliente.HomeClient.domain.entities.ParkingLot
import com.example.parkingtop.features.cliente.HomeClient.domain.entities.UserProfile
import com.example.parkingtop.features.cliente.HomeClient.domain.usecases.GetNearbyParkingsUseCase
import com.example.parkingtop.features.cliente.HomeClient.domain.usecases.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = false,
    val userProfile: UserProfile? = null,
    val nearbyParkings: List<ParkingLot> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class HomeClientViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getNearbyParkingsUseCase: GetNearbyParkingsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        loadData()
    }

    fun loadData(lat: Double = -34.6037, lng: Double = -58.3816) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            val profileResult = getUserProfileUseCase()
            val parkingsResult = getNearbyParkingsUseCase(lat, lng)

            _state.value = _state.value.copy(
                isLoading = false,
                userProfile = profileResult.getOrNull(),
                nearbyParkings = parkingsResult.getOrDefault(emptyList()),
                error = if (profileResult.isFailure) profileResult.exceptionOrNull()?.message else null
            )
        }
    }
}
