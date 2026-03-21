package com.example.parkingtop.features.login.data.repositories

import com.example.parkingtop.core.network.ParkingApi
import com.example.parkingtop.core.network.model.LoginRequest
import com.example.parkingtop.features.login.data.datasources.mapper.toDomain
import com.example.parkingtop.features.login.domain.entities.AuthResult
import com.example.parkingtop.features.login.domain.repositories.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val api: ParkingApi
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<AuthResult> {
        return try {
            val response = api.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body()?.data != null) {
                // Aquí usamos el mapper que mapea el AuthResponseDTO de register ya que es el mismo
                // pero si quieres ser estricto, podrías definir uno en login.
                // Como ParkingApi usa el AuthResponseDTO de register para login también:
                val data = response.body()!!.data!!
                
                // Mapeo manual si no queremos depender del otro feature o usar un mapper común
                Result.success(
                    AuthResult(
                        user = com.example.parkingtop.features.login.domain.entities.User(
                            id = data.user.id,
                            email = data.user.email,
                            fullName = data.user.fullName,
                            role = data.user.role,
                            profileImageUrl = data.user.profileImage
                        ),
                        token = data.token,
                        refreshToken = data.refreshToken
                    )
                )
            } else {
                Result.failure(Exception(response.body()?.error?.message ?: "Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
