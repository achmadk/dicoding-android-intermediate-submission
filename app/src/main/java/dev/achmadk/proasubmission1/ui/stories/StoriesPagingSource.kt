package dev.achmadk.proasubmission1.ui.stories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.services.DicodingStory
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

const val STARTING_PAGE_INDEX = 1

class StoriesPagingSource @Inject constructor(
    private val dicodingStory: DicodingStory,
    private val token: String,
    private val location: Int?
): PagingSource<Int, StoryResponseBody>() {
    override fun getRefreshKey(state: PagingState<Int, StoryResponseBody>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun getOptions(params: LoadParams<Int>, location: Int?): Map<String, Int> {
        val result: HashMap<String, Int> = HashMap()
        result["page"] = params.key ?: STARTING_PAGE_INDEX
        result["size"] = params.loadSize
        result["location"] = location ?: 0
        return result
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseBody> {
        return try {
            val options = getOptions(params, location)
            val response = dicodingStory.getStories(token, options)
            val stories = response.body()!!.listStory
            val prevKey = if (options["page"] == STARTING_PAGE_INDEX) null else options["page"]?.minus(1)
            val nextKey = if (stories.isEmpty()) null else options["page"]?.plus(1)
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