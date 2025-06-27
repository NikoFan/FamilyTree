package com.example.familytree.AddTreeActions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familytree.AddTreeActions.ui.theme.FamilyTreeTheme
import com.example.familytree.Database.DatabaseConnectClass
import com.example.familytree.MainPage
import com.example.familytree.Widgets
import com.example.familytree.R
import com.example.familytree.StaticStorage
import com.example.familytree.Widgets.ErrorSnackbar
import com.example.familytree.Widgets.Input
import com.example.familytree.Widgets.callSnackBar
import com.example.familytree.WorkWithJSON.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EmptyTreeFirstPersonCreate : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyTreeTheme {
                Constructor()
            }
        }
    }

    @Composable
    fun Constructor() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            Widgets.SetImage(R.drawable.account_icon)
            Widgets.WindowTitle("Create yourself")
            CreateAccountInputForm(
                LocalContext.current,
                TreeBodyUpdaterActivity::class.java
            )
            Widgets.UnderButton(
                "Back",
                TreesViewActivity::class.java,
                LocalContext.current
            )

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CreateAccountInputForm(
        context: Context,
        nextActivityClass: Class<out Activity>
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        var accountNameInput by remember { mutableStateOf("") }
        var accountFullNameInput by remember { mutableStateOf("") }
        var accountBirthdayInput by remember { mutableStateOf("") }
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var accountSexInput by remember { mutableStateOf("") }
        var showDatePicker by remember { mutableStateOf(false) }

        // Опции для выпадающего списка
        val options = listOf("Male", "Female")
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // NAME INPUT
            Input(
                labelText = "Input person's name",
                value = accountNameInput,
                OnValueChange = { newText -> accountNameInput = newText }
            )

            // FIO INPUT
            Input(
                labelText = "Input person's full name",
                value = accountFullNameInput,
                OnValueChange = { newText -> accountFullNameInput = newText }
            )

            // DATE INPUT
            OutlinedTextField(
                value = accountBirthdayInput,
                onValueChange = { newText -> accountBirthdayInput = newText },
                label = { Text(text = "Date") },
                modifier = Modifier
                    .padding(stringResource(R.string.padding).toInt().dp)
                    .fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Choose date")
                    }
                }
            )
            // SEX INPUT
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable() { accountSexInput = option }
                ) {
                    RadioButton(
                        selected = (option == accountSexInput),
                        onClick = { accountSexInput = option }
                    )
                    Text(
                        text = option,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }


            Button(
                modifier = Modifier
                    .padding(stringResource(R.string.accept_button_padding).toInt().dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(
                        R.color.button_color
                    )
                ),
                shape = RectangleShape,
                onClick = {
                    // Проверка что поля для ввода не пустые
                    if (
                        accountFullNameInput.length != 0 &&
                        accountNameInput.length != 0 &&
                        accountSexInput.length != 0 &&
                        accountBirthdayInput.length != 0
                    ) {
                        println("Данные верны")
                        var db: DatabaseConnectClass = DatabaseConnectClass(context)
                        var treeBody: String = db.getTreeBody()
                        var jsonWriter: JsonWriter = JsonWriter()
                        db.UpdateTreeBody(
                            jsonWriter.CreateStartJSONView(
                                jsonStringBefore = treeBody,
                                inputPersonData = listOf(
                                    accountNameInput,
                                    accountFullNameInput,
                                    accountSexInput,
                                    accountBirthdayInput
                                )
                            )
                        )

                        StaticStorage.setPersonId(1)

                        context.startActivity(
                            Intent(
                                context,
                                nextActivityClass
                            )
                        )
                    } else {
                        callSnackBar(
                            "Please input some data!",
                            snackbarHostState,
                            scope
                        )
                    }
                }
            ) {
                Text(
                    text = "Create person",
                    color = Color.White,
                    fontSize = stringResource(R.string.button_text_size).toInt().sp
                )
            }

        }


        // Диалог выбора даты
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                val date = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
                                accountBirthdayInput = date.format(dateFormatter)
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        ErrorSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier
                .padding(8.dp)
        )
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FamilyTreeTheme {
            Constructor()
        }
    }
}

