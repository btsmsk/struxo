package com.struxo.kit.feature.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.struxo.kit.core.presentation.components.AppTextField
import com.struxo.kit.core.presentation.components.LoadingButton
import com.struxo.kit.core.presentation.theme.Spacing

/**
 * Stateful forgot password screen that collects ViewModel state and effects.
 *
 * @param viewModel ForgotPassword ViewModel instance.
 * @param onNavigateBack Called when the user taps back or after success acknowledgment.
 */
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ForgotPasswordEffect.NavigateBack -> onNavigateBack()
                is ForgotPasswordEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.isSuccess) {
            ForgotPasswordSuccessContent(
                onBack = { viewModel.onEvent(ForgotPasswordEvent.BackClicked) },
                modifier = Modifier.padding(padding),
            )
        } else {
            ForgotPasswordFormContent(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

/**
 * Email input form for password reset.
 *
 * @param state Current [ForgotPasswordState].
 * @param onEvent Callback for user actions.
 * @param modifier Optional [Modifier].
 */
@Composable
fun ForgotPasswordFormContent(
    state: ForgotPasswordState,
    onEvent: (ForgotPasswordEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = "Enter your email and we'll send you a reset link",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Spacing.xxl))

        AppTextField(
            value = state.email,
            onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
            label = "Email",
            error = state.emailError,
            placeholder = "you@example.com",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onEvent(ForgotPasswordEvent.SubmitClicked)
                },
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Spacing.xl))

        LoadingButton(
            text = "Send Reset Link",
            onClick = { onEvent(ForgotPasswordEvent.SubmitClicked) },
            isLoading = state.isLoading,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(Spacing.md))

        OutlinedButton(
            onClick = { onEvent(ForgotPasswordEvent.BackClicked) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back to Sign In")
        }
    }
}

/**
 * Success message shown after the reset email is sent.
 *
 * @param onBack Called when the user taps the back button.
 * @param modifier Optional [Modifier].
 */
@Composable
fun ForgotPasswordSuccessContent(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.lg),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Check Your Email",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(Spacing.sm))
        Text(
            text = "We've sent a password reset link to your email address. Please check your inbox and follow the instructions.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(Spacing.xxl))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Back to Sign In")
        }
    }
}
