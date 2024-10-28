package com.aregyan.compose

import android.app.Application
import com.aregyan.compose.util.PrefUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

val prefUtil: PrefUtil by lazy {
    ComposeApplication.prefs!!
}

@HiltAndroidApp
class ComposeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        prefs = PrefUtil(applicationContext)
    }

    companion object {
        var prefs: PrefUtil? = null
        lateinit var instance: ComposeApplication
            private set
    }

}