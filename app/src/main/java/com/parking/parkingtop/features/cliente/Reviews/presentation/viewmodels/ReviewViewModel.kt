package com.parking.parkingtop.features.cliente.Reviews.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.parking.parkingtop.features.cliente.Reviews.domain.usecases.CreateReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReviewUiState(
    val isLoading: Boolean  = false,
    val isSuccess: Boolean  = false,
    val error: String?      = null
)

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val createReviewUseCase: CreateReviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewUiState())
    val state: StateFlow<ReviewUiState> = _state.asStateFlow()

    fun submitReview(
        parkingLotId: String,
        reservationId: String,
        rating: Int,
        comment: String?
    ) {
        viewModelScope.launch {
            _state.value = ReviewUiState(isLoading = true)

            createReviewUseCase.execute(parkingLotId, reservationId, rating, comment).fold(
                onSuccess = { _state.value = ReviewUiState(isSuccess = true) },
                onFailure = { e -> _state.value = ReviewUiState(error = e.message) }
            )
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}