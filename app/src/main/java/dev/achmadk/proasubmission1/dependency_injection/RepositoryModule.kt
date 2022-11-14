package dev.achmadk.proasubmission1.dependency_injection

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.achmadk.proasubmission1.data.repositories.DataStoreRepository
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.data.repositories.UserPreferenceRepository
import dev.achmadk.proasubmission1.services.DicodingStory
import dev.achmadk.proasubmission1.ui.create_story.repositories.CreateStoryRepository
import dev.achmadk.proasubmission1.ui.create_story.repositories.ICreateStoryRepository
import dev.achmadk.proasubmission1.ui.login.repositories.ILoginRepository
import dev.achmadk.proasubmission1.ui.login.repositories.LoginRepository
import dev.achmadk.proasubmission1.ui.register.repositories.IRegisterRepository
import dev.achmadk.proasubmission1.ui.register.repositories.RegisterRepository
import dev.achmadk.proasubmission1.ui.stories.repositories.IStoriesRepository
import dev.achmadk.proasubmission1.ui.stories.repositories.StoriesRepository
import dev.achmadk.proasubmission1.ui.story_locations.repositories.IStoryLocationsRepository
import dev.achmadk.proasubmission1.ui.story_locations.repositories.StoryLocationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext appContext: Context) = DataStoreRepository(appContext)

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(dataStoreRepository: DataStoreRepository): IUserPreferenceRepository = UserPreferenceRepository(dataStoreRepository)

    @Provides
    @Singleton
    fun provideCreateStoryRepository(dicodingStory: DicodingStory, iUserPreferenceRepository: IUserPreferenceRepository): ICreateStoryRepository = CreateStoryRepository(dicodingStory, iUserPreferenceRepository)

    @Provides
    @Singleton
    fun provideStoriesRepository(dicodingStory: DicodingStory, iUserPreferenceRepository: IUserPreferenceRepository): IStoriesRepository = StoriesRepository(dicodingStory, iUserPreferenceRepository)

    @Provides
    @Singleton
    fun provideRegisterRepository(dicodingStory: DicodingStory): IRegisterRepository = RegisterRepository(dicodingStory)

    @Provides
    @Singleton
    fun provideLoginRepository(dicodingStory: DicodingStory): ILoginRepository = LoginRepository(dicodingStory)

    @Provides
    @Singleton
    fun provideStoryLocationsRepository(iStoriesRepository: IStoriesRepository, iUserPreferenceRepository: IUserPreferenceRepository): IStoryLocationsRepository = StoryLocationRepository(iStoriesRepository, iUserPreferenceRepository)
}