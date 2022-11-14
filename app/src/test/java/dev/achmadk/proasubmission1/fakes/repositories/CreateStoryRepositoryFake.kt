package dev.achmadk.proasubmission1.fakes.repositories

import dev.achmadk.proasubmission1.models.BaseResponseBody
import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import dev.achmadk.proasubmission1.ui.create_story.repositories.ICreateStoryRepository
import retrofit2.Response
import java.io.File

class CreateStoryRepositoryFake: ICreateStoryRepository {
    override suspend fun submitStory(
        initialRequestBody: StoryRequestBodyCreate,
        initialImageFile: File
    ): Response<BaseResponseBody> = Response.success(
        BaseResponseBody(
            error = false,
            message = "successfully submit story"
        )
    )
}