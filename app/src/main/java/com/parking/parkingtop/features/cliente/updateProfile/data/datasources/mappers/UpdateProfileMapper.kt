package com.parking.parkingtop.features.cliente.updateProfile.data.datasources.mappers

import com.parking.parkingtop.features.cliente.Perfil.data.datasources.models.UserDTO
import com.parking.parkingtop.features.cliente.updateProfile.domain.entities.UserProfile


fun UserDTO.toUserProfile() = UserProfile(
    id = id,
    fullName = fullName,
    email = email,
    phone = phone,
    profileImageUrl = profileImageUrl
)