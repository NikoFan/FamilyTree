package com.example.familytree

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familytree.Database.DatabaseConnectClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.familytree.WorkWithJSON.JsonWriter

object Widgets {

    // Переменная для подтверждения действий пользователя
    var acceptValue: Boolean = false

    // Изображение над заголовком
    @Composable
    fun SetImage(
        imageId: Int
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageId),
                modifier = Modifier
                    .size(128.dp)
                    .padding(
                        bottom = 16.dp
                    )
                    .fillMaxWidth(),
                contentDescription = "logo"
            )
        }
    }



    fun callSnackBar(
        messageText: String,
        snackbarHostState: SnackbarHostState,
        scope: CoroutineScope
    ) {

        scope.launch {
            snackbarHostState.showSnackbar(
                message = messageText,
                duration = SnackbarDuration.Short
            )
        }
    }

    // Инструкция под заголовком
    @Composable
    fun InstructionText(
        instructionContent: String,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = instructionContent,
                fontSize = (stringResource(R.string.title_text_size).toInt() / 2.5).sp,
                fontStyle = FontStyle.Italic
            )
        }

    }

    // Кнопка под основной
    @Composable
    fun UnderButton(
        buttonName: String,
        nextClassActivity: Class<out Activity>,
        context: Context
    ) {

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = (stringResource(R.string.accept_button_padding).toInt() * 2).dp,
                    end = (stringResource(R.string.accept_button_padding).toInt() * 2).dp

                ),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = {
                context.startActivity(
                    Intent(
                        context,
                        nextClassActivity
                    )
                )
            }
        ) {
            Text(
                text = buttonName,
                color = Color.Black,
                fontSize = stringResource(R.string.under_button_text_size).toInt().sp
            )
        }
    }

    // Поле для ввода в форме
    @Composable
    fun Input(
        labelText: String,
        value: String,
        OnValueChange: (String) -> Unit
    ) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    stringResource(R.string.padding).toInt().dp
                ),
            value = value,
            textStyle = TextStyle(
                fontSize = stringResource(R.string.input_text_size).toInt().sp
            ),
            onValueChange = OnValueChange,
            label = {
                Text(
                    text = labelText,
                    modifier = Modifier
                        .padding(
                            start = 20.dp
                        ),
                    fontSize = stringResource(R.string.input_text_size).toInt().sp
                )
            }
        )
    }


    // Заголовок окон
    @Composable
    fun WindowTitle(
        titleText: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = titleText,
                fontSize = stringResource(R.string.title_text_size).toInt().sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    // Всплывающее сообщение ОШИБКА
    @Composable
    fun ErrorSnackbar(
        snackbarHostState: SnackbarHostState,
        modifier: Modifier = Modifier
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        ) { data ->
            Snackbar(
                containerColor = colorResource(R.color.discard_color),
                contentColor = Color.White,
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            text = data.visuals.message,
                            fontSize = stringResource(R.string.snackbar_text_size).toInt().sp,

                            )
                    }

                }
            )
        }
    }

}