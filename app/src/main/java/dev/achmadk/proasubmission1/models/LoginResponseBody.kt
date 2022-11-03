package dev.achmadk.proasubmission1.models

data class LoginResponseBodyOnlyLoginResult(
    val userId: String,
    val name: String,
    val token: String
)

data class LoginResponseBody(
    var loginResult: LoginResponseBodyOnlyLoginResult,
    override var error: Boolean,
    override var message: String
): BaseResponseBodyAbstract()
