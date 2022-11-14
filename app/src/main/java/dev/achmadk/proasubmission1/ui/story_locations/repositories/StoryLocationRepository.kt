package dev.achmadk.proasubmission1.ui.story_locations.repositories

import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.ui.stories.repositories.IStoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryLocationRepository @Inject constructor(
    private val storyRepository: IStoriesRepository,
    private val userPreferenceRepository: IUserPreferenceRepository
): IStoryLocationsRepository {
    override suspend fun getStoryLocations(options: Map<String, Int>?) = withContext(
        Dispatchers.IO
    ) {
        val token = userPreferenceRepository.getToken()
        return@withContext storyRepository.getStories(token, options)
    }
}