package com.playlab.escaperoomtimer.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.playlab.escaperoomtimer.R
import com.playlab.escaperoomtimer.ui.theme.EscapeRoomTimerTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TimeInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body1
        .copy(textAlign = TextAlign.Center),
    text: String,
    placeholder: String = "00",
    fontSize: TextUnit = dimensionResource(id = R.dimen.input_text_font_size).value.sp,
    maxLength: Int = 2,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    onValueChange: (String) -> Unit = {}
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier.clickable {  onClick() }
            .width(100.dp)
            .clip(MaterialTheme.shapes.medium),
        value = text,
        readOnly = readOnly,
        enabled = enabled,
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = fontSize,
                text = placeholder,
                textAlign = TextAlign.Center,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center)
            )
        },
        singleLine = true,
        textStyle = textStyle.copy(fontSize = fontSize),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            disabledIndicatorColor = Color.Unspecified
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        onValueChange = {
            if (it.length <= maxLength && it.isDigitsOnly()) {
                onValueChange(it)
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecretCodeInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body1
        .copy(textAlign = TextAlign.Center),
    text: String,
    fontSize: TextUnit = dimensionResource(id = R.dimen.input_text_font_size).value.sp,
    placeholder: String = "********",
    maxLength: Int = 10,
    onValueChange: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = modifier
            .width(dimensionResource(id = R.dimen.input_secret_code_size))
            .clip(MaterialTheme.shapes.medium),
        value = text,
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                fontSize = fontSize,
                text = placeholder,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center)
            )
        },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        textStyle = textStyle.copy(fontSize = fontSize),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colors.onPrimary,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            disabledIndicatorColor = Color.Unspecified
        ),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        onValueChange = {
            if (it.length <= maxLength && it.isDigitsOnly()) {
                onValueChange(it)
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BigSecretCodeInput(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.body1
        .copy(
            textAlign = TextAlign.Center,
        ),
    fontSize: TextUnit = dimensionResource(id = R.dimen.secret_code_font_size).value.sp,
    text: String,
    placeholder: String = "******",
    textColor: Color = MaterialTheme.colors.onPrimary,
    maxLength: Int = 10,
    readOnly: Boolean = false,
    onValueChange: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        value = text,
        placeholder = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = placeholder,
                fontSize = fontSize,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center)
            )
        },
        readOnly = readOnly,
        singleLine = true,
        textStyle = textStyle.copy(fontSize = fontSize, color = textColor),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Unspecified,
            focusedIndicatorColor = Color.Unspecified,
            unfocusedIndicatorColor = Color.Unspecified,
            disabledIndicatorColor = Color.Unspecified
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        onValueChange = {
            if (it.length <= maxLength && it.isDigitsOnly()) {
                onValueChange(it)
            }
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSecretCodeInput() {
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface (){

            var text by remember { mutableStateOf("") }

            SecretCodeInput(
                modifier = Modifier
                    .padding(40.dp),
                text = text,
                onValueChange = {
                    text = it
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBigSecretCodeInput() {
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface (){

            var text by remember { mutableStateOf("") }

            BigSecretCodeInput(

                text = text,
                onValueChange = {
                    text = it
                }
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTimeInput() {
    EscapeRoomTimerTheme(darkTheme = true) {
        Surface (){

            var text by remember { mutableStateOf("") }

            TimeInput(
                modifier = Modifier
                    .padding(80.dp)
                    .size(dimensionResource(id = R.dimen.input_time_size)),
                text = text,
                onValueChange = {
                    text = it
                }
            )
        }
    }
}