package dev.achmadk.proasubmission1.ui.stories.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.stories.repositories.IStoriesRepository
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.hasInternetConnection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val storiesRepository: IStoriesRepository,
    private val userPreferenceRepository: IUserPreferenceRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    @Suppress("MemberVisibilityCanBePrivate")
    val storiesResponse: MutableLiveData<Resource<List<StoryResponseBody>>> = MutableLiveData()

    @Suppress("MemberVisibilityCanBePrivate")
    val storiesPaging: Flow<PagingData<StoryResponseBody>> = storiesRepository.getStoriesPaging().cachedIn(viewModelScope)

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
                    Timber.tag("internet_connected").i("Successfully connected to internet")
                    val token = userPreferenceRepository.getToken()
                    val response = storiesRepository.getStories(token, options)
                    if (response.isSuccessful) {
                        Timber.tag("get_stories_success").d(response.toString())
                        val storiesResponseData = response.body()!!
                        Timber.tag("token_saved").i("Successfully save user data")
                        storiesResponse.postValue(Resource.Success(storiesResponseData.listStory))
                    } else {
                        val gson = Gson()
                        val type = object : TypeToken<StoriesResponseBody>() {}.type
                        val errorResponse: StoriesResponseBody? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        storiesResponse.postValue(
                            Resource.Error(
                                errorResponse?.message ?: "Unknown Error"
                            )
                        )
                    }
                } else {
                    Timber.tag("get_stories_failed").e("No internet connection")
                    storiesResponse.postValue(Resource.Error("No internet connection"))
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                when (ex) {
                    is IOException -> storiesResponse.postValue(Resource.Error("Network failure: ${ex.localizedMessage}"))
                    else -> storiesResponse.postValue(Resource.Error("Conversion error"))
                }
                Timber.tag("get_stories_failed").e("Failed to get stories")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferenceRepository.logout()
        }
    }
}