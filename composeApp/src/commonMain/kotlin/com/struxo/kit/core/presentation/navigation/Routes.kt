package com.struxo.kit.core.presentation.navigation

import kotlinx.serialization.Serializable

/** Login screen route — app start destination when unauthenticated. */
@Serializable
data object LoginRoute

/** Home screen route — post-authentication landing page. */
@Serializable
data object HomeRoute
