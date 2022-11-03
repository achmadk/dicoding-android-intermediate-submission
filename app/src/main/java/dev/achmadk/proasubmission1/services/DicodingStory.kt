package dev.achmadk.proasubmission1.services

import dev.achmadk.proasubmission1.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface DicodingStory {
    companion object {
        const val ENDPOINT = "https://story-api.dicoding.dev/v1/"
    }

    @Headers("Content-Type: application/json")
    @POST("register")
    suspend fun register(@Body request: RegisterRequestBody): Response<RegisterResponseBody>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(@Body request: LoginRequestBody): Response<LoginResponseBody>

    @GET("stories")
    suspend fun getStories(@Header("Authorization") authorization: String, @QueryMap options: Map<String, Int>?): Response<StoriesResponseBody>

    @GET("stories/{id}")
    suspend fun getStoryDetails(@Path("id") id: String, @Header("Authorization") authorization: String): Response<StoryDetailsResponseBody>

    @Multipart
    @POST("stories")
    @JvmSuppressWildcards
    suspend fun submitStory(@PartMap params: Map<String, RequestBody>, @Part photo: MultipartBody.Part, @Header("Authorization") authorization: String): Response<BaseResponseBody>
}