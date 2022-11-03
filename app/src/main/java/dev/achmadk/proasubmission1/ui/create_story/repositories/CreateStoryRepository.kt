package dev.achmadk.proasubmission1.ui.create_story.repositories

import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import dev.achmadk.proasubmission1.services.DicodingStory
import dev.achmadk.proasubmission1.utils.reduceImageFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateStoryRepository @Inject constructor (
    private val dicodingStory: DicodingStory
) {
    private fun generateRequestBody(initialRequestBody: StoryRequestBodyCreate): MutableMap<String, RequestBody> {
        val requestBody: MutableMap<String, RequestBody> = mutableMapOf()
        requestBody["description"] = initialRequestBody.description.toRequestBody("text/plain".toMediaType())
//        if (initialRequestBody.lat?.isNaN() == false) {
//            requestBody["lat"] = initialRequestBody.lat ?: 0f
//        }
//        requestBody["lon"] = initialRequestBody.lon ?: 0f
        return requestBody
    }

    suspend fun submitStory(initialRequestBody: StoryRequestBodyCreate, initialImageFile: File, token: String) = withContext(Dispatchers.IO) {
        val requestBody: MutableMap<String, RequestBody> = generateRequestBody(initialRequestBody)
        val compressedImageFile = reduceImageFile(initialImageFile)
        val imageFile = MultipartBody.Part.createFormData("photo", compressedImageFile.name, compressedImageFile.asRequestBody())
        return@withContext dicodingStory.submitStory(requestBody, imageFile, token)
    }
}