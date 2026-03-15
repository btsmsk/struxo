package com.struxo.kit.di

import com.struxo.kit.core.data.local.AppDatabase
import com.struxo.kit.core.data.network.ApiClient
import com.struxo.kit.feature.auth.data.local.AuthLocalSource
import com.struxo.kit.feature.auth.data.remote.AuthApi
import com.struxo.kit.feature.auth.data.repository.AuthRepositoryImpl
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import com.struxo.kit.feature.auth.domain.usecase.LoginUseCase
import com.struxo.kit.feature.auth.presentation.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Core infrastructure module — HTTP client and API client.
 */
val coreModule = module {
    single {
        ApiClient(
            baseUrl = "https://api.example.com/v1/",
            engine = get(),
            tokenProvider = get(),
        )
    }
}

/**
 * Auth feature module — data sources, repository, use case, and ViewModel.
 */
val authModule = module {
    single { AuthApi(get()) }
    single { get<AppDatabase>().authUserDao() }
    single { AuthLocalSource(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    factory { LoginUseCase(get()) }
    viewModel { LoginViewModel(get()) }
}

/**
 * All Koin modules for the application.
 *
 * Order: platform (engines, DB, tokens) → core (HTTP client) → feature modules.
 */
val appModules = listOf(platformModule, coreModule, authModule)
