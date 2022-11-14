@file:Suppress("MemberVisibilityCanBePrivate")

package dev.achmadk.proasubmission1.ui.story_locations.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.story_locations.repositories.IStoryLocationsRepository
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.hasInternetConnection
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class StoryLocationViewModel @Inject constructor(
    private val storyLocationsRepository: IStoryLocationsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val storiesResponse: MutableLiveData<Resource<List<StoryResponseBody>>> = MutableLiveData()

    private fun getOptions(): Map<String, Int> {
        val result: MutableMap<String, Int> = mutableMapOf()
        result["location"] = 1
        return result
    }

    fun getStoryLocations(options: Map<String, Int>? = getOptions()) {
        viewModelScope.launch {
            try {
                if (hasInternetConnection(context)) {
                    val response = storyLocationsRepository.getStoryLocations(options)
                    if (response.isSuccessful) {
                        val storiesResponseData = response.body()!!
                        storiesResponse.postValue(Resource.Success(storiesResponseData.listStory))
                    }  else {
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
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> storiesResponse.postValue(Resource.Error("Network failure: " + ex.localizedMessage))
                    else -> storiesResponse.postValue(Resource.Error("Conversion error"))
                }
            }

        }
    }
}