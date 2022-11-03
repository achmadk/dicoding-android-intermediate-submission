package dev.achmadk.proasubmission1.services

interface UserPreferences {
    companion object {
        const val NAME = "USER_PREFERENCES"

        const val KEY_USER_ID = "USER_ID"
        const val KEY_USER_NAME = "USER_NAME"
        const val KEY_USER_TOKEN = "USER_TOKEN"
    }
}