package dev.achmadk.proasubmission1.fakes.repositories

import androidx.paging.*
import dev.achmadk.proasubmission1.models.StoriesResponseBody
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.stories.NETWORK_PAGE_SIZE
import dev.achmadk.proasubmission1.ui.stories.STARTING_PAGE_INDEX
import dev.achmadk.proasubmission1.ui.stories.repositories.IStoriesRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class StoriesRepositoryFake: IStoriesRepository {
    private fun getFallbackOptions(): Map<String, Int> {
        val result: HashMap<String, Int> = HashMap()
        result["page"] = 1
        result["size"] = 20
        result["location"] = 0
        return result
    }

    override suspend fun getStories(
        token: String,
        options: Map<String, Int>?
    ): Response<StoriesResponseBody> {
        val listStory: MutableList<StoryResponseBody> = mutableListOf()
        val usedOptions = options ?: getFallbackOptions()
        if (usedOptions["page"]!! < 3) {
            for (index in 1..10) {
                listStory.add(
                    StoryResponseBody(
                        name = "User $index",
                        description = "Photo description $index",
                        photoUrl = "https://photostock.id/$index",
                        createdAt = "2022-11-11 13:52",
                        id = "story_$index"
                    )
                )
            }
        }
        return Response.success(StoriesResponseBody(
            error = false,
            message = "successfully get stories",
            listStory = listStory
        ))
    }

    override fun getStoriesPaging(): Flow<PagingData<StoryResponseBody>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true,
                prefetchDistance = 4 * NETWORK_PAGE_SIZE
            ),
            pagingSourceFactory = {
                getStoriesPagingSource()
            }
        ).flow
    }

    inner class StoriesPagingSourceFake : PagingSource<Int, StoryResponseBody>() {
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
                val response = getStories("", options)
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

    private fun getStoriesPagingSource(): PagingSource<Int, StoryResponseBody> {
        return StoriesPagingSourceFake()
    }
}
