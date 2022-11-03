package dev.achmadk.proasubmission1.models

data class StoriesRequestBody(
    var page: Int = 1,
    var size: Int = 10,
    /** only allow value 0 or 1 */
    var location: Int = 0
)