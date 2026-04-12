package com.parking.parkingtop.features.subscritionplan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.core.datastore.TokenDataStore
import com.parking.parkingtop.core.network.model.SubscriptionPlanDto
import com.parking.parkingtop.features.subscritionplan.data.repositories.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscritionPlanViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val tokenDataStore: TokenDataStore
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
                    _errorMessage.value = response.body()?.error?.message ?: "Error al cargar planes"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error de conexión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectPlan(planId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = tokenDataStore.accessToken.first() ?: ""
                val response = repository.createSubscription(token, planId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val url = response.body()?.data?.paymentUrl
                    if (!url.isNullOrBlank()) {
                        _paymentUrl.emit(url)
                    } else {
                        // Si no hay URL, tal vez sea un plan gratuito o ya esté suscrito
                        // Podríamos navegar al home directamente
                        _paymentUrl.emit("navigate_home")
                    }
                } else {
                    _errorMessage.value = response.body()?.error?.message ?: "Error al procesar suscripción"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error de conexión"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
