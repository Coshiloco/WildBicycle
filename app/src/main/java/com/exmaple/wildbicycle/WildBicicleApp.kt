package com.exmaple.wildbicycle

import android.app.Application
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WildBicicleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Linea que comprueba si el dispositivo es compatible con material you
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
