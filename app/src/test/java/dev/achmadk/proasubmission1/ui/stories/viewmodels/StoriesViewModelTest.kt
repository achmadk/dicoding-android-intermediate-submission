package dev.achmadk.proasubmission1.ui.stories.viewmodels

import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import dev.achmadk.proasubmission1.fakes.repositories.StoriesRepositoryFake
import dev.achmadk.proasubmission1.fakes.repositories.UserPreferenceRepositoryFake
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.stories.StoryItemCallback
import dev.achmadk.proasubmission1.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StoriesViewModelTest: BaseViewModelTest() {
    private lateinit var iStoriesRepository: StoriesRepositoryFake
    private lateinit var iUserPreferenceRepository: UserPreferenceRepositoryFake
    private lateinit var storiesViewModel: StoriesViewModel

    @Before
    fun setUp() {
        iStoriesRepository = StoriesRepositoryFake()
        iUserPreferenceRepository = UserPreferenceRepositoryFake()
        storiesViewModel = StoriesViewModel(
            iStoriesRepository,
            iUserPreferenceRepository,
            ContextFake
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get stories successfully`() = runTest {
        val observer = Observer<Resource<List<StoryResponseBody>>> {}
        try {
            val actualStoriesResponse = storiesViewModel.storiesResponse.observeForever(observer)

            storiesViewModel.getStories()

            assertNotNull(actualStoriesResponse)
        } finally {
            storiesViewModel.storiesResponse.removeObserver(observer)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should successfully receive 20 items of stories with paging`() = runTest {
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryItemCallback(),
            updateCallback = NoopListCallback(),
            workerDispatcher = Dispatchers.Main
        )
        val job = launch {
            storiesViewModel.storiesPaging.collectLatest {
                differ.submitData(it)
            }
        }
        try {
            advanceUntilIdle()
            assertEquals(differ.snapshot().items.size, 20)
        } finally {
            job.cancel()
        }
    }
}