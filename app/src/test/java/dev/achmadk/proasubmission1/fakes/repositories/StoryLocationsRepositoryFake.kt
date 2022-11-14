package dev.achmadk.proasubmission1.fakes.repositories

import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.story_locations.repositories.IStoryLocationsRepository
import retrofit2.Response

class StoryLocationsRepositoryFake(): IStoryLocationsRepository {
    override suspend fun getStoryLocations(options: Map<String, Int>?): Response<StoriesResponseBody> = Response.success(
        StoriesResponseBody(
            error = false,
            message = "successfully get stories",
            listStory = listOf(
                StoryResponseBody(
                    id = "photo_id_12345",
                    name = "story 1",
                    description = "story description 1",
                    lat = 100.100,
                    lon = -20.123,
                    photoUrl = "https://google.com/img/test_1234.jpg",
                    createdAt = "2022-11-11 15:12:35"
                )
            )
        )
    )
}