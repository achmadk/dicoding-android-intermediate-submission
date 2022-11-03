package dev.achmadk.proasubmission1.ui.stories.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.achmadk.proasubmission1.data.repositories.UserPreferenceRepository
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.stories.repositories.StoriesRepository
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.hasInternetConnection
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val storiesRepository: StoriesRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    @Suppress("MemberVisibilityCanBePrivate")
    val storiesResponse: MutableLiveData<Resource<List<StoryResponseBody>>> = MutableLiveData()

    suspend fun getStoriesPaging(location: Int? = 0): LiveData<PagingData<StoryResponseBody>> {
        val token = userPreferenceRepository.getToken()
        return storiesRepository.getStoriesPaging(token, location).cachedIn(viewModelScope)
    }

    private fun getFallbackOptions(): Map<String, Int> {
        val result: HashMap<String, Int> = HashMap()
        result["page"] = 1
        result["size"] = 20
        result["location"] = 0
        return result
    }

    fun getStories(options: Map<String, Int>? = getFallbackOptions()) {
        storiesResponse.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    Log.i("internet_connected", "Successfully connected to internet")
                    val token = userPreferenceRepository.getToken()
                    val response = storiesRepository.getStories(token, options)
                    if (response.isSuccessful) {
                        Log.d("get_stories_success", response.toString())
                        val storiesResponseData = response.body()!!
                        Log.i("token_saved", "Successfully save user data")
                        storiesResponse.postValue(Resource.Success(storiesResponseData.listStory))
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StoriesResponseBody>() {}.type
                        val errorResponse: StoriesResponseBody? = gson.fromJson(response.errorBody()!!.charStream(), type)
                        storiesResponse.postValue(Resource.Error(errorResponse?.message ?: "Unknown Error"))
                    }
                } else {
                    Log.e("get_stories_failed", "No internet connection")
                    storiesResponse.postValue(Resource.Error("No internet connection"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                when (ex) {
                    is IOException -> storiesResponse.postValue(Resource.Error("Network failure: ${ex.localizedMessage}"))
                    else -> storiesResponse.postValue(Resource.Error("Conversion error"))
                }
                Log.e("get_stories_failed", "Failed to get stories")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferenceRepository.logout()
        }
    }
}