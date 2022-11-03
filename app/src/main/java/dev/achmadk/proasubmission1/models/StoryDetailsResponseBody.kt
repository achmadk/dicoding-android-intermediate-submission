package dev.achmadk.proasubmission1.models

data class StoryDetailsResponseBody(
    override var error: Boolean,
    override var message: String,
    var story: StoryResponseBody
): BaseResponseBodyAbstract()