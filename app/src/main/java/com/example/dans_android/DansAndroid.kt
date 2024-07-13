package com.example.dans_android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DansAndroid : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}