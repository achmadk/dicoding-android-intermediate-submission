package dev.achmadk.proasubmission1.ui.stories.repositories

import androidx.paging.Pager
import androidx.paging.PagingData
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IStoriesRepository {
    suspend fun getStories(token: String, options: Map<String, Int>?): Response<StoriesResponseBody>
    fun getStoriesPaging(): Flow<PagingData<StoryResponseBody>>
}