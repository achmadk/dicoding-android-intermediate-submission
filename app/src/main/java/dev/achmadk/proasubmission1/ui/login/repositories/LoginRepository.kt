package dev.achmadk.proasubmission1.ui.login.repositories

import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.models.LoginResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(
    private val dicodingStory: DicodingStory
): ILoginRepository {
    override suspend fun submitLogin(body: LoginRequestBody): Response<LoginResponseBody> = withContext(
        Dispatchers.IO
    ) {
        val loginResult = dicodingStory.login(body)
        loginResult
    }
}