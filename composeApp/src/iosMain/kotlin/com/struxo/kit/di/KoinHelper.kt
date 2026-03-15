package com.struxo.kit.di

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.context.startKoin

/**
 * Helper object exposed to Swift for Koin initialisation.
 *
 * Call `KoinHelper.shared.doInitKoin()` from the SwiftUI `App.init()`.
 */
object KoinHelper {

    fun doInitKoin() {
        startKoin {
            modules(appModules)
        }
        Napier.base(DebugAntilog())
    }
}
