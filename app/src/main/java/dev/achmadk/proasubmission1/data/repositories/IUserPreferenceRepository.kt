package dev.achmadk.proasubmission1.data.repositories

import dev.achmadk.proasubmission1.models.LoginResponseBodyOnlyLoginResult

interface IUserPreferenceRepository {
    suspend fun getToken(): String

    suspend fun logout()

    suspend fun setUser(data: LoginResponseBodyOnlyLoginResult)
}