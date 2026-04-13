package com.parking.parkingtop.features.subscritionplan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.model.ApiResponse
import com.parking.parkingtop.core.network.model.SubscriptionPlanDto
import com.parking.parkingtop.features.subscritionplan.data.repositories.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SubscritionPlanViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val tokenDataStore: TokenDataStore,
    private val json: Json
) : ViewModel() {

    private val _plans = MutableStateFlow<List<SubscriptionPlanDto>>(emptyList())
    val plans: StateFlow<List<SubscriptionPlanDto>> = _plans

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _paymentUrl = MutableSharedFlow<String>()
    val paymentUrl = _paymentUrl.asSharedFlow()

    init {
        loadPlans()
    }

    fun loadPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = tokenDataStore.accessToken.first() ?: ""
                val response = repository.getPlans(token)
                if (response.isSuccessful && response.body()?.success == true) {
                    _plans.value = response.body()?.data ?: emptyList()
                } else {
                    _errorMessage.value = parseError(response.errorBody()?.string(), response.body())
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPlan(planId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val token = tokenDataStore.accessToken.first() ?: ""
                val response = repository.createSubscription(token, planId)
                
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.success == true) {
                        val url = body.data?.paymentUrl
                        if (!url.isNullOrBlank()) {
                            _paymentUrl.emit(url)
                        } else {
                            _paymentUrl.emit("navigate_home")
                        }
                    } else {
                        _errorMessage.value = body?.error?.message ?: "Error desconocido en el servidor"
                    }
                } else {
                    // Manejar errores 4xx/5xx con detalle
                    val errorDetail = parseError(response.errorBody()?.string(), response.body())
                    _errorMessage.value = "Error al procesar: $errorDetail"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun parseError(errorBody: String?, apiResponse: ApiResponse<*>?): String {
        return try {
            if (apiResponse?.error?.message != null) {
                apiResponse.error.message
            } else if (!errorBody.isNullOrBlank()) {
                val parsed = json.decodeFromString<ApiResponse<Unit>>(errorBody)
                parsed.error?.message ?: "Error en el servidor"
            } else {
                "Error inesperado"
            }
        } catch (e: Exception) {
            "Error del servidor (Código de respuesta inválido)"
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
