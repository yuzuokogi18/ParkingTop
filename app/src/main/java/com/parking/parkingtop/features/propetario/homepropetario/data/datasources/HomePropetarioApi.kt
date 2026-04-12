package com.parking.parkingtop.features.propetario.homepropetario.data.datasources

import com.parking.parkingtop.features.propetario.homepropetario.data.datasources.models.HomePropetarioResponse
import retrofit2.http.GET

interface HomePropetarioApi {
    @GET("owner/home")
    suspend fun getHomeData(): HomePropetarioResponse
}
