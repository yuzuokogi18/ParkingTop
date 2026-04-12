package com.parking.parkingtop.features.cliente.Perfil.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.hardware.data.CameraManager
import com.parking.parkingtop.core.network.model.OwnerBalanceDto
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Reservation
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.User
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.entities.Vehicle
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.repositories.ProfileRepository
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.DeleteVehicleUseCase
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetProfileUseCase
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetReservationsUseCase
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.GetVehiclesUseCase
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.LogoutUseCase
import com.parking.parkingtop.features.cliente.Perfil.presentation.domain.usecases.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val isLoading: Boolean                    = false,
    val user: User?                           = null,
    val vehicles: List<Vehicle>               = emptyList(),
    val activeReservations: List<Reservation> = emptyList(),
    val reservationHistory: List<Reservation> = emptyList(),
    val balance: OwnerBalanceDto?             = null,
    val error: String?                        = null,
    val isUploadingImage: Boolean             = false,
    val isUpdatingProfile: Boolean            = false,
    val updateSuccess: Boolean                = false,
    val logoutSuccess: Boolean                = false,
    val cameraAvailable: Boolean              = false,
    val frontCameraAvailable: Boolean         = false,
    val showCameraPermissionRationale: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase:      GetProfileUseCase,
    private val updateProfileUseCase:   UpdateProfileUseCase,
    private val getVehiclesUseCase:     GetVehiclesUseCase,
    private val getReservationsUseCase: GetReservationsUseCase,
    private val logoutUseCase:          LogoutUseCase,
    private val deleteVehicleUseCase:   DeleteVehicleUseCase,
    private val profileRepository:      ProfileRepository,
    private val cameraManager:          CameraManager
) : ViewModel() {

    private val _state = mutableStateOf(ProfileState())
    val state = _state

    init {
        checkCameraCapabilities()
        loadProfileData()
    }

    private fun checkCameraCapabilities() {
        _state.value = _state.value.copy(
            cameraAvailable      = cameraManager.hasCamera(),
            frontCameraAvailable = cameraManager.hasFrontCamera()
        )
    }

    fun onCameraPermissionDenied() {
        _state.value = _state.value.copy(showCameraPermissionRationale = true)
    }

    fun dismissCameraRationale() {
        _state.value = _state.value.copy(showCameraPermissionRationale = false)
    }

    fun loadProfileData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Perfil
            getProfileUseCase().fold(
                onSuccess = { user ->
                    _state.value = _state.value.copy(user = user)
                    if (user.role == "owner") loadOwnerBalance()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        error = error.message ?: "Error al cargar perfil"
                    )
                }
            )

            // Vehículos
            getVehiclesUseCase().fold(
                onSuccess = { vehicles ->
                    _state.value = _state.value.copy(vehicles = vehicles)
                },
                onFailure = { }
            )

            // ✅ Reservas — una sola llamada, filtra localmente por status
            getReservationsUseCase().fold(
                onSuccess = { reservations ->
                    _state.value = _state.value.copy(
                        activeReservations = reservations.filter { reservation ->
                            reservation.status in listOf("confirmed", "active", "pending")
                        },
                        reservationHistory = reservations.filter { reservation ->
                            reservation.status in listOf("completed", "cancelled", "no_show")
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
            deleteVehicleUseCase.execute(vehicleId).onSuccess {
                _state.value = _state.value.copy(
                    vehicles = _state.value.vehicles.filter { it.id != vehicleId }
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    error = e.message ?: "Error al eliminar el vehículo"
                )
            }
        }
    }
}