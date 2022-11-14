package dev.achmadk.proasubmission1.ui.story_locations.viewmodels

import androidx.lifecycle.Observer
import dev.achmadk.proasubmission1.fakes.repositories.StoryLocationsRepositoryFake
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.story_locations.repositories.IStoryLocationsRepository
import dev.achmadk.proasubmission1.utils.BaseViewModelTest
import dev.achmadk.proasubmission1.utils.ContextFake
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StoryLocationViewModelTest: BaseViewModelTest() {
    private lateinit var iStoryLocationsRepository: IStoryLocationsRepository
    private lateinit var storyLocationViewModel: StoryLocationViewModel

    @Before
    fun setUp() {
        iStoryLocationsRepository = StoryLocationsRepositoryFake()
        storyLocationViewModel = StoryLocationViewModel(
            iStoryLocationsRepository,
            ContextFake
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get stories successfully`() = runTest {
        val observer = Observer<Resource<List<StoryResponseBody>>> {}
        try {
            val actualStoriesResponse = storyLocationViewModel.storiesResponse.observeForever(observer)

            storyLocationViewModel.getStoryLocations()
            advanceUntilIdle()

            assertEquals(storyLocationViewModel.storiesResponse.value!!.data!!.size, 1)

            assertNotNull(actualStoriesResponse)
        } finally {
            storyLocationViewModel.storiesResponse.removeObserver(observer)
        }

    }
}