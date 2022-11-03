package dev.achmadk.proasubmission1.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.achmadk.proasubmission1.dao.DicodingStoryDao
import dev.achmadk.proasubmission1.dependency_injection.ApplicationScope
import dev.achmadk.proasubmission1.models.StoryResponseBody
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [StoryResponseBody::class], version = 1)
abstract class DicodingStoryDatabase: RoomDatabase() {

    abstract fun getDicodingStoryDao(): DicodingStoryDao

    class Callback @Inject constructor(
        private val database: Provider<DicodingStoryDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback()
}