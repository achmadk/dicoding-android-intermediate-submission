package dev.achmadk.proasubmission1.ui.stories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 1

const val NETWORK_PAGE_SIZE = 20

class StoriesPagingSource @Inject constructor(
    private val dicodingStory: DicodingStory,
    private val userPreferenceRepository: IUserPreferenceRepository,
): PagingSource<Int, StoryResponseBody>() {
    override val jumpingSupported: Boolean = true

    override fun getRefreshKey(state: PagingState<Int, StoryResponseBody>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    private fun getOptions(params: LoadParams<Int>): Map<String, Int> {
        val result: HashMap<String, Int> = HashMap()
        result["page"] = params.key ?: STARTING_PAGE_INDEX
        result["size"] = NETWORK_PAGE_SIZE
        result["location"] = 0
        return result
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseBody> {
        return try {
            val options = getOptions(params)
            val token = userPreferenceRepository.getToken()
            val response = dicodingStory.getStories(token, options)
            val nextPage = params.key ?: 1
            val stories = response.body()!!.listStory
            val prevKey = if (options["page"] == STARTING_PAGE_INDEX) null else nextPage - 1
            val nextKey = if (stories.isEmpty()) null else nextPage + 1
            LoadResult.Page(
                data = stories,
                prevKey,
                nextKey
            )

        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}