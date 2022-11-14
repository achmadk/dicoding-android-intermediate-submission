package dev.achmadk.proasubmission1.ui.story_locations.repositories

import dev.achmadk.proasubmission1.models.StoriesResponseBody
import retrofit2.Response

interface IStoryLocationsRepository {
    suspend fun getStoryLocations(options: Map<String, Int>?): Response<StoriesResponseBody>
}