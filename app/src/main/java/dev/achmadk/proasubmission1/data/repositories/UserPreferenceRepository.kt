package dev.achmadk.proasubmission1.data.repositories

import dev.achmadk.proasubmission1.models.LoginResponseBodyOnlyLoginResult
import dev.achmadk.proasubmission1.services.UserPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferenceRepository @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun getToken(): String {
        val savedUserToken = dataStoreRepository.getString(UserPreferences.KEY_USER_TOKEN)
        return if (!savedUserToken.isNullOrBlank()) "Bearer $savedUserToken" else ""
    }

    suspend fun logout() {
        dataStoreRepository.putString(UserPreferences.KEY_USER_TOKEN, "")
        dataStoreRepository.putString(UserPreferences.KEY_USER_NAME, "")
        dataStoreRepository.putString(UserPreferences.KEY_USER_ID, "")
    }

    suspend fun setUser(data: LoginResponseBodyOnlyLoginResult) {
        dataStoreRepository.putString(UserPreferences.KEY_USER_ID, data.userId)
        dataStoreRepository.putString(UserPreferences.KEY_USER_NAME, data.name)
        dataStoreRepository.putString(UserPreferences.KEY_USER_TOKEN, data.token)
    }
}