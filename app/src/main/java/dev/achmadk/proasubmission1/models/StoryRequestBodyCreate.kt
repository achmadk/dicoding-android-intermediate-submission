package dev.achmadk.proasubmission1.models

data class StoryRequestBodyCreate(
    var description: String,
    var lat: Float? = 0f,
    var lon: Float? = 0f
)