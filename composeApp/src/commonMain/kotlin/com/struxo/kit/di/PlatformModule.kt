package com.struxo.kit.di

import org.koin.core.module.Module

/**
 * Platform-specific Koin module providing implementations for:
 * - [io.ktor.client.engine.HttpClientEngine]
 * - [com.struxo.kit.core.data.local.AppDatabase]
 * - [com.struxo.kit.core.data.network.TokenProvider]
 */
expect val platformModule: Module
