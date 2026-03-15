package com.struxo.kit.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import com.struxo.kit.core.presentation.theme.Spacing

/**
 * Styled [OutlinedTextField] with built-in error display.
 *
 * When [error] is non-null, the field switches to error styling and the
 * message is shown below the field.
 *
 * @param value Current text value.
 * @param onValueChange Text change callback.
 * @param label Label text displayed inside the field.
 * @param modifier Optional [Modifier].
 * @param error Error message, or `null` for the normal state.
 * @param placeholder Placeholder text when the field is empty.
 * @param visualTransformation Transformation applied to the input (e.g. password masking).
 * @param keyboardOptions IME keyboard configuration.
 * @param keyboardActions IME action callbacks.
 * @param singleLine Whether the field is single-line.
 * @param enabled Whether the field is editable.
 * @param trailingIcon Optional trailing icon composable.
 */
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            enabled = enabled,
            trailingIcon = trailingIcon,
            modifier = Modifier.fillMaxWidth(),
        )
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = Spacing.md, top = Spacing.xs),
            )
        }
    }
}
