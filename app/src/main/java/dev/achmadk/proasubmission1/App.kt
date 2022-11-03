package dev.achmadk.proasubmission1

import android.app.Application
import android.os.Build
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application(), ImageLoaderFactory {
    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivitiesIfAvailable(this)
        }
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .build()
    }
}