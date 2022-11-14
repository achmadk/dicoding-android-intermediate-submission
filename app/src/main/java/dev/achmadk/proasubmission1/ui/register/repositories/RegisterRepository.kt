package dev.achmadk.proasubmission1.ui.register.repositories

import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.models.RegisterResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegisterRepository @Inject constructor(
    private val dicodingStory: DicodingStory
): IRegisterRepository {
    override suspend fun submitRegister(body: RegisterRequestBody): Response<RegisterResponseBody> = withContext(
        Dispatchers.IO
    ) {
        val registerResult = dicodingStory.register(body)
        registerResult
    }
}