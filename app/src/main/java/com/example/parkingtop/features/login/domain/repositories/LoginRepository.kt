package com.example.parkingtop.features.login.domain.repositories

import com.example.parkingtop.features.login.domain.entities.AuthResult

interface LoginRepository {
    suspend fun login(email: String, password: String): Result<AuthResult>
}
