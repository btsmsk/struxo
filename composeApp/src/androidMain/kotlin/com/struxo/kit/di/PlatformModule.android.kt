package com.struxo.kit.di

import com.struxo.kit.core.data.local.getDatabaseBuilder
import com.struxo.kit.core.data.network.TokenProvider
import com.struxo.kit.core.data.network.TokenProviderImpl
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android platform module providing:
 * - OkHttp engine for Ktor
 * - Room database built with Android Context
 * - SharedPreferences-backed TokenProvider
 */
actual val platformModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { getDatabaseBuilder(androidContext()).build() }
    single<TokenProvider> { TokenProviderImpl(androidContext()) }
}
