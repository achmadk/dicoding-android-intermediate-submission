package dev.achmadk.proasubmission1.ui.stories.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import dev.achmadk.proasubmission1.ui.stories.StoriesPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoriesRepository @Inject constructor(
    private val dicodingStory: DicodingStory
) {
    suspend fun getStories(token: String, options: Map<String, Int>?): Response<StoriesResponseBody> = withContext(
        Dispatchers.IO
    ) {
        val storiesResult = dicodingStory.getStories(token, options)
        storiesResult
    }

    suspend fun getStoriesPaging(token: String, location: Int?): LiveData<PagingData<StoryResponseBody>> = withContext(Dispatchers.IO) {
        return@withContext Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = {
                StoriesPagingSource(
                    dicodingStory,
                    token,
                    location
                )
            }
        ).liveData
    }
}