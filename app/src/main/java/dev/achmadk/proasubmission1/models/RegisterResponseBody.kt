package dev.achmadk.proasubmission1.models

data class RegisterResponseBody(
    override var error: Boolean,
    override var message: String
): BaseResponseBodyAbstract()
