package dev.achmadk.proasubmission1.ui.register.repositories

import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.models.RegisterResponseBody
import retrofit2.Response

interface IRegisterRepository {
    suspend fun submitRegister(body: RegisterRequestBody): Response<RegisterResponseBody>
}