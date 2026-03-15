package com.struxo.kit.di

import com.struxo.kit.core.data.local.AppDatabase
import com.struxo.kit.core.data.network.ApiClient
import com.struxo.kit.feature.auth.data.local.AuthLocalSource
import com.struxo.kit.feature.auth.data.remote.AuthApi
import com.struxo.kit.feature.auth.data.repository.FakeAuthRepository
import com.struxo.kit.feature.auth.domain.repository.AuthRepository
import com.struxo.kit.feature.auth.domain.usecase.ForgotPasswordUseCase
import com.struxo.kit.feature.auth.domain.usecase.GetCurrentUserUseCase
import com.struxo.kit.feature.auth.domain.usecase.LoginUseCase
import com.struxo.kit.feature.auth.domain.usecase.LogoutUseCase
import com.struxo.kit.feature.auth.domain.usecase.RegisterUseCase
import com.struxo.kit.feature.auth.presentation.ForgotPasswordViewModel
import com.struxo.kit.feature.auth.presentation.LoginViewModel
import com.struxo.kit.feature.auth.presentation.RegisterViewModel
import com.struxo.kit.feature.home.presentation.HomeViewModel
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
 * Auth feature module — data sources, repository, use cases, and ViewModels.
 */
val authModule = module {
    single { AuthApi(get()) }
    single { get<AppDatabase>().authUserDao() }
    single { AuthLocalSource(get()) }
    // TODO: Replace with AuthRepositoryImpl(get(), get(), get()) when real API is available
    single<AuthRepository> { FakeAuthRepository(get(), get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { LogoutUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }
    factory { ForgotPasswordUseCase(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
}

/**
 * Home feature module — ViewModel.
 */
val homeModule = module {
    viewModel { HomeViewModel(get(), get()) }
}

/**
 * All Koin modules for the application.
 *
 * Order: platform (engines, DB, tokens) → core (HTTP client) → feature modules.
 */
val appModules = listOf(platformModule, coreModule, authModule, homeModule)
