package dev.achmadk.proasubmission1.ui.register.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.models.RegisterResponseBody
import dev.achmadk.proasubmission1.ui.register.repositories.IRegisterRepository
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.hasInternetConnection
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: IRegisterRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    @Suppress("MemberVisibilityCanBePrivate")
    val registerResponse: MutableLiveData<Resource<RegisterResponseBody>> = MutableLiveData()

    fun submitRegister(body: RegisterRequestBody) {
        registerResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    val response = registerRepository.submitRegister(body)
                    if (response.isSuccessful) {
                        val registerResult = response.body()!!
                        registerResponse.postValue(Resource.Success(registerResult))
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<RegisterResponseBody>() {}.type
                        val errorResponse: RegisterResponseBody? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        registerResponse.postValue(Resource.Error(errorResponse?.message ?: "Unknown Error"))
                    }
                } else {
                    Timber.tag("submit_register_failed").e("No internet connection")
                }
            } catch (ex: Exception) {
                Timber.tag("submit_register_failed").e("Failed to submit register form")
            }
        }
    }
}