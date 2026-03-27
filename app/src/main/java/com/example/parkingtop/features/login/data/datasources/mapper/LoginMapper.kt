package com.example.parkingtop.features.login.data.datasources.mapper

import com.example.parkingtop.features.login.data.datasources.models.AuthResponseDTO
import com.example.parkingtop.features.login.domain.entities.AuthResult
import com.example.parkingtop.features.login.domain.entities.User

fun AuthResponseDTO.toDomain(): AuthResult {
    return AuthResult(
        user = User(
            id = this.user.id,
            email = this.user.email,
            fullName = this.user.fullName,
            role = this.user.role,
            profileImageUrl = this.user.profileImageUrl
        ),
        token = this.token,
        refreshToken = this.refreshToken
    )
}
