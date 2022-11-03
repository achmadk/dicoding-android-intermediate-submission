package dev.achmadk.proasubmission1.ui.create_story.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.achmadk.proasubmission1.data.repositories.UserPreferenceRepository
import dev.achmadk.proasubmission1.models.BaseResponseBody
import dev.achmadk.proasubmission1.models.BaseResponseBodyAbstract
import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import dev.achmadk.proasubmission1.ui.create_story.repositories.CreateStoryRepository
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
@HiltViewModel
class CreateStoryViewModel @Inject constructor(
    private val createStoryRepository: CreateStoryRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {
    val isSuccessSubmitStory: MutableLiveData<Boolean?> = MutableLiveData(null)

    val submitStoryResponse: MutableLiveData<Resource<BaseResponseBody>> = MutableLiveData()

    fun submitStory(payload: StoryRequestBodyCreate, file: File) {
        submitStoryResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                val token = userPreferenceRepository.getToken()
                val response = createStoryRepository.submitStory(payload, file, token)
                if (response.isSuccessful) {
                    isSuccessSubmitStory.postValue(true)
                    val body = response.body()!!
                    submitStoryResponse.postValue(Resource.Success(body))
                } else {
                    val gson = Gson()
                    val type = object : TypeToken<BaseResponseBodyAbstract>() {}.type
                    val errorResponse: BaseResponseBodyAbstract? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    submitStoryResponse.postValue(Resource.Error(errorResponse?.message ?: "Unknown Error"))
                    isSuccessSubmitStory.postValue(false)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                isSuccessSubmitStory.postValue(false)
                when (ex) {
                    is IOException -> submitStoryResponse.postValue(Resource.Error("Network failure: ${ex.localizedMessage}"))
                    else -> submitStoryResponse.postValue(Resource.Error("Conversion error"))
                }
            }
        }
    }
}