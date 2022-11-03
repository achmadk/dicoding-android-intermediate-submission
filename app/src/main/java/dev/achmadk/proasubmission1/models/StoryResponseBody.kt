package dev.achmadk.proasubmission1.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story_response_body")
data class StoryResponseBody (
    var name: String,
    var description: String,
    var photoUrl: String,
    var createdAt: String,
    var lat: Float? = null,
    var lon: Float? = null,
    @PrimaryKey
    var id: String
): Parcelable