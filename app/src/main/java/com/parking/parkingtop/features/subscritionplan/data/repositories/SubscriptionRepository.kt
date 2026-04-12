package com.parking.parkingtop.features.subscritionplan.data.repositories

import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.core.network.model.ApiResponse
import com.parking.parkingtop.core.network.model.CreateSubscriptionRequest
import com.parking.parkingtop.core.network.model.CreateSubscriptionResultDto
import com.parking.parkingtop.core.network.model.SubscriptionPlanDto
import retrofit2.Response
import javax.inject.Inject

class SubscriptionRepository @Inject constructor(
    private val api: ParkingApi
) {
    suspend fun getPlans(token: String): Response<ApiResponse<List<SubscriptionPlanDto>>> {
        return api.getPlans("Bearer $token")
    }

    suspend fun createSubscription(token: String, planId: String): Response<ApiResponse<CreateSubscriptionResultDto>> {
        return api.createSubscription("Bearer $token", CreateSubscriptionRequest(planId))
    }
}
