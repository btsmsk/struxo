package com.struxo.kit.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.struxo.kit.feature.auth.presentation.ForgotPasswordScreen
import com.struxo.kit.feature.auth.presentation.LoginScreen
import com.struxo.kit.feature.auth.presentation.RegisterScreen
import com.struxo.kit.feature.home.presentation.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

/**
 * Root navigation graph for the application.
 *
 * Starts at [LoginRoute] and navigates to [HomeRoute] after successful login.
 *
 * @param modifier Optional [Modifier] applied to the [NavHost].
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier,
    ) {
        composable<LoginRoute> {
            LoginScreen(
                viewModel = koinViewModel(),
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterRoute)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(ForgotPasswordRoute)
                },
            )
        }

        composable<HomeRoute> {
            HomeScreen(
                viewModel = koinViewModel(),
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo<HomeRoute> { inclusive = true }
                    }
                },
            )
        }

        composable<RegisterRoute> {
            RegisterScreen(
                viewModel = koinViewModel(),
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
            )
        }

        composable<ForgotPasswordRoute> {
            ForgotPasswordScreen(
                viewModel = koinViewModel(),
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
