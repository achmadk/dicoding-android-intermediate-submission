package dev.achmadk.proasubmission1.fakes.repositories

import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.models.RegisterResponseBody
import dev.achmadk.proasubmission1.ui.register.repositories.IRegisterRepository
import retrofit2.Response

class RegisterRepositoryFake(): IRegisterRepository {
    override suspend fun submitRegister(body: RegisterRequestBody): Response<RegisterResponseBody> {
        return Response.success(RegisterResponseBody(
            error = false,
            message = "your account has been registered successfully"
        ))
    }
}