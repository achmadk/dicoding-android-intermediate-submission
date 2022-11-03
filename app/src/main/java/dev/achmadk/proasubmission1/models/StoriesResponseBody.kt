package dev.achmadk.proasubmission1.models

data class StoriesResponseBody(
    override var error: Boolean,
    override var message: String,
    var listStory: List<StoryResponseBody>
): BaseResponseBodyAbstract()