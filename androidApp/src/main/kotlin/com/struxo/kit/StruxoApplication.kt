package com.struxo.kit

import android.app.Application
import com.struxo.kit.di.appModules
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application entry point for Android.
 *
 * Initialises Koin dependency injection and Napier logging.
 */
class StruxoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@StruxoApplication)
            modules(appModules)
        }
        Napier.base(DebugAntilog())
    }
}
