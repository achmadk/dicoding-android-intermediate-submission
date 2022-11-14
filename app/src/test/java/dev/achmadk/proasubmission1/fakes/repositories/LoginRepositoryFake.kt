package dev.achmadk.proasubmission1.fakes.repositories

import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.models.LoginResponseBody
import dev.achmadk.proasubmission1.models.LoginResponseBodyOnlyLoginResult
import dev.achmadk.proasubmission1.ui.login.repositories.ILoginRepository
import retrofit2.Response

class LoginRepositoryFake: ILoginRepository {
    override suspend fun submitLogin(body: LoginRequestBody): Response<LoginResponseBody> = Response.success(
        SAMPLE_LOGIN_RESPONSE_BODY
    )

    companion object {
        val SAMPLE_LOGIN_RESPONSE_BODY: LoginResponseBody = LoginResponseBody(
            loginResult = LoginResponseBodyOnlyLoginResult(
                userId = "12345",
                name = "Hello world",
                token = "sample_token_12345"
            ),
            error = false,
            message = "login successfully"
        )
    }
}