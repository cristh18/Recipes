package com.tolodev.recipes

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp(MultiDexApplication::class)
class RecipesApplication : Hilt_RecipesApplication() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}