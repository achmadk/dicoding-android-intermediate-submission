package dev.achmadk.proasubmission1.models

data class RegisterRequestBody(
    var name: String,
    var email: String,
    var password: String
)
