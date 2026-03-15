package com.struxo.kit.di

import com.struxo.kit.core.data.local.getDatabaseBuilder
import com.struxo.kit.core.data.network.TokenProvider
import com.struxo.kit.core.data.network.TokenProviderImpl
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/**
 * iOS platform module providing:
 * - Darwin engine for Ktor
 * - Room database built with NSHomeDirectory path
 * - NSUserDefaults-backed TokenProvider
 */
actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    single { getDatabaseBuilder().build() }
    single<TokenProvider> { TokenProviderImpl() }
}
