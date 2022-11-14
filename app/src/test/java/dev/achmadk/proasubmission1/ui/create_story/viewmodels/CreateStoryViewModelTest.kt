package dev.achmadk.proasubmission1.ui.create_story.viewmodels

import androidx.lifecycle.Observer
import dev.achmadk.proasubmission1.fakes.repositories.CreateStoryRepositoryFake
import dev.achmadk.proasubmission1.models.BaseResponseBody
import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import dev.achmadk.proasubmission1.ui.create_story.repositories.ICreateStoryRepository
import dev.achmadk.proasubmission1.utils.BaseViewModelTest
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

class CreateStoryViewModelTest: BaseViewModelTest() {
    private lateinit var createStoryRepository: ICreateStoryRepository
    private lateinit var createStoryViewModel: CreateStoryViewModel

    @Before
    fun setUp() {
        createStoryRepository = CreateStoryRepositoryFake()
        createStoryViewModel = CreateStoryViewModel(
            createStoryRepository
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should submit story successfully`() = runTest {
        val observer = Observer<Resource<BaseResponseBody>> {}
        try {
            val actualLoginResponse = createStoryViewModel.submitStoryResponse.observeForever(observer)

            val body = StoryRequestBodyCreate(
                description = "I have beautiful time"
            )
            val file = File("test.jpg")
            createStoryViewModel.submitStory(body, file)
            advanceUntilIdle()

            assertEquals(createStoryViewModel.submitStoryResponse.value!!.javaClass, Resource.Success::class.java)
            assertEquals(createStoryViewModel.isSuccessSubmitStory.value, true)

            assertNotNull(actualLoginResponse)
        } finally {
            createStoryViewModel.submitStoryResponse.removeObserver(observer)
        }
    }
}