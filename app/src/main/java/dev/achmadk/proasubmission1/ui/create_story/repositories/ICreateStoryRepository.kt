package dev.achmadk.proasubmission1.ui.create_story.repositories

import dev.achmadk.proasubmission1.models.BaseResponseBody
import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import retrofit2.Response
import java.io.File

interface ICreateStoryRepository {
    suspend fun submitStory(initialRequestBody: StoryRequestBodyCreate, initialImageFile: File): Response<BaseResponseBody>
}