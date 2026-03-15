package com.struxo.kit.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.struxo.kit.core.presentation.theme.Spacing

/**
 * [Button] that shows a [CircularProgressIndicator] when [isLoading] is true.
 *
 * The button is disabled during loading to prevent duplicate submissions.
 *
 * @param text Button label.
 * @param onClick Click callback.
 * @param modifier Optional [Modifier].
 * @param isLoading Whether the loading spinner should be shown.
 * @param enabled Whether the button is enabled (independent of loading state).
 */
@Composable
fun LoadingButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
            Spacer(Modifier.width(Spacing.sm))
        }
        Text(text = text)
    }
}
