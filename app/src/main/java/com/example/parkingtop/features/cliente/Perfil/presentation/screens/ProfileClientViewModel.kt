package com.example.parkingtop.features.cliente.Perfil.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkingtop.core.network.model.OwnerBalanceDto
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.DeleteVehicleUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetProfileUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetReservationsUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetVehiclesUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.LogoutUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.usecases.UpdateProfileUseCase
import com.example.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val vehicles: List<Vehicle> = emptyList(),
    val activeReservations: List<Reservation> = emptyList(),
    val reservationHistory: List<Reservation> = emptyList(),
    val balance: OwnerBalanceDto? = null,
    val error: String? = null,
    val isUploadingImage: Boolean = false,
    val isUpdatingProfile: Boolean = false,
    val updateSuccess: Boolean = false,
    val logoutSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase:      GetProfileUseCase,
    private val updateProfileUseCase:   UpdateProfileUseCase,
    private val getVehiclesUseCase:     GetVehiclesUseCase,
    private val getReservationsUseCase: GetReservationsUseCase,
    private val logoutUseCase:          LogoutUseCase,
    private val deleteVehicleUseCase:   DeleteVehicleUseCase,
    private val profileRepository:      ProfileRepository
) : ViewModel() {

    private val _state = mutableStateOf(ProfileState())
    val state = _state

    init {
        loadProfileData()
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            getProfileUseCase().fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(user = user)
                    // Si es owner, cargar balance
                    if (user.role == "owner") {
                        loadOwnerBalance()
                    }
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Error al cargar perfil"
                    )
                }
            )

            getVehiclesUseCase().fold(
                onSuccess = { vehicles ->
                    _state.value = _state.value.copy(vehicles = vehicles)
                },
                onFailure = { }
            )

            getReservationsUseCase("active").fold(
                onSuccess = { reservations ->
                    _state.value = _state.value.copy(
                        activeReservations = reservations.filter {
                            it.status == "confirmed" || it.status == "active"
                        }
                    )
                },
                onFailure = { }
            )

            getReservationsUseCase("completed").fold(
                onSuccess = { reservations ->
                    _state.value = _state.value.copy(
                        reservationHistory = reservations.filter {
                            it.status == "completed" || it.status == "cancelled"
                        }
                    )
                },
                onFailure = { }
            )

            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private fun loadOwnerBalance() {
        viewModelScope.launch {
            profileRepository.getOwnerBalance().onSuccess { balance ->
                _state.value = _state.value.copy(balance = balance)
            }.onFailure { 
                // Silently fail or handle error
            }
        }
    }

    fun updateProfile(fullName: String, phone: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isUpdatingProfile = true, updateSuccess = false)

            updateProfileUseCase(fullName, phone).fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(
                        user              = user,
                        isUpdatingProfile = false,
                        updateSuccess     = true
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        error             = error.message ?: "Error al actualizar perfil",
                        isUpdatingProfile = false
                    )
                }
            )
        }
    }

    fun deleteVehicle(vehicleId: String) {
        viewModelScope.launch {
            val result = deleteVehicleUseCase.execute(vehicleId)
            result.onSuccess {
                _state.value = _state.value.copy(
                    vehicles = _state.value.vehicles.filter { it.id != vehicleId }
                )
            }
            result.onFailure { e ->
                _state.value = _state.value.copy(
                    error = e.message ?: "Error al eliminar el vehículo"
                )
            }
        }
    }
}