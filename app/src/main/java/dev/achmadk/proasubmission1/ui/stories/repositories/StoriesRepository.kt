package dev.achmadk.proasubmission1.ui.stories.repositories

import androidx.paging.*
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import dev.achmadk.proasubmission1.ui.stories.NETWORK_PAGE_SIZE
import dev.achmadk.proasubmission1.ui.stories.StoriesPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoriesRepository @Inject constructor(
    private val dicodingStory: DicodingStory,
    private val userPreferenceRepository: IUserPreferenceRepository
): IStoriesRepository {
    override suspend fun getStories(token: String, options: Map<String, Int>?): Response<StoriesResponseBody> = withContext(
        Dispatchers.IO
    ) {
        val storiesResult = dicodingStory.getStories(token, options)
        storiesResult
    }

    override fun getStoriesPaging(): Flow<PagingData<StoryResponseBody>> = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = true
        ),
        pagingSourceFactory = {
            StoriesPagingSource(
                dicodingStory,
                userPreferenceRepository
            )
        }
    ).flow
}