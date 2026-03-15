package com.struxo.kit

import androidx.compose.runtime.Composable
import com.struxo.kit.core.presentation.navigation.AppNavigation
import com.struxo.kit.core.presentation.theme.AppTheme

/**
 * Root composable for the Struxo application.
 *
 * Koin is initialised in platform entry points via `startKoin {}`,
 * which automatically sets up the Compose integration in Koin 4.x.
 */
@Composable
fun App() {
    AppTheme {
        AppNavigation()
    }
}
