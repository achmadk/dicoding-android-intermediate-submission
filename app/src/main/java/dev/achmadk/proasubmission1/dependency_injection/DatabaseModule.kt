package dev.achmadk.proasubmission1.dependency_injection

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.achmadk.proasubmission1.dao.DicodingStoryDao
import dev.achmadk.proasubmission1.db.DicodingStoryDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: DicodingStoryDatabase.Callback): DicodingStoryDatabase {
        return Room.databaseBuilder(application, DicodingStoryDatabase::class.java, "dicoding_story_db")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideDicodingStoryDao(db: DicodingStoryDatabase): DicodingStoryDao {
        return db.getDicodingStoryDao()
    }
}