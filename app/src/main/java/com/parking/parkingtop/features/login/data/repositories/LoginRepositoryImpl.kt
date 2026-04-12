package com.parking.parkingtop.features.login.data.repositories

import com.parking.parkingtop.core.network.ParkingApi
import com.parking.parkingtop.core.network.model.LoginRequest
import com.parking.parkingtop.features.login.domain.entities.AuthResult
import com.parking.parkingtop.features.login.domain.repositories.LoginRepository
import com.parking.parkingtop.features.login.domain.entities.User
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
                        user = User(
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
