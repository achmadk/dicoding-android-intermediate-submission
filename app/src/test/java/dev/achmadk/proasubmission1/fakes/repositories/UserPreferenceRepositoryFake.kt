package dev.achmadk.proasubmission1.fakes.repositories

import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.models.LoginResponseBodyOnlyLoginResult

class UserPreferenceRepositoryFake: IUserPreferenceRepository {
    var token: String? = null

    override suspend fun getToken(): String {
        return token ?: ""
    }

    override suspend fun logout() {
        token = null
    }

    override suspend fun setUser(data: LoginResponseBodyOnlyLoginResult) {
        token = data.token
    }
}