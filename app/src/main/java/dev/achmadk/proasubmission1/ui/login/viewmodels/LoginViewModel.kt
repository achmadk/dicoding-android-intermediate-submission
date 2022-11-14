package dev.achmadk.proasubmission1.ui.login.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.models.LoginResponseBody
import dev.achmadk.proasubmission1.ui.login.repositories.ILoginRepository
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.hasInternetConnection
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: ILoginRepository,
    private val userPreferenceRepository: IUserPreferenceRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    @Suppress("MemberVisibilityCanBePrivate")
    val loginResponse: MutableLiveData<Resource<LoginResponseBody>> = MutableLiveData()

    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkUserDataExist() {
        viewModelScope.launch {
            val userToken = userPreferenceRepository.getToken()
            isLoggedIn.postValue(userToken.isNotEmpty())
        }
    }

    fun submitLogin(body: LoginRequestBody) {
        loginResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    Timber.tag("internet_connected").i("Successfully connected to internet")
                    val response = loginRepository.submitLogin(body)
                    if (response.isSuccessful) {
                        Timber.tag("login_response_success").d(response.toString())
                        val loginResponseData = response.body()!!
                        userPreferenceRepository.setUser(loginResponseData.loginResult)
                        Timber.tag("token_saved").i("Successfully save user data")
                        loginResponse.postValue(Resource.Success(loginResponseData))
                        isLoggedIn.postValue(true)
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<LoginResponseBody>() {}.type
                        val errorResponse: LoginResponseBody? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        loginResponse.postValue(
                            Resource.Error(
                                errorResponse?.message ?: "Unknown Error"
                            )
                        )
                    }
                } else {
                    Timber.tag("submit_login_failed").e("No internet connection")
                    loginResponse.postValue(Resource.Error("No internet connection"))
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> loginResponse.postValue(Resource.Error("Network failure: " + ex.localizedMessage))
                    else -> loginResponse.postValue(Resource.Error("Conversion error"))
                }
                Timber.tag("submit_login_failed").e("Failed to submit register form")
            }
        }
    }
}