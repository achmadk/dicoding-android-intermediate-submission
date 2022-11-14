package dev.achmadk.proasubmission1.ui.login.repositories

import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.models.LoginResponseBody
import retrofit2.Response

interface ILoginRepository {
    suspend fun submitLogin(body: LoginRequestBody): Response<LoginResponseBody>
}