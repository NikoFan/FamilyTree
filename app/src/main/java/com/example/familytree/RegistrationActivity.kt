package com.example.familytree

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familytree.Database.DatabaseConnectClass
import com.example.familytree.ui.theme.FamilyTreeTheme
import com.example.familytree.LogInActivity
import com.example.familytree.InputDataSecurity
import kotlinx.coroutines.launch
import com.example.familytree.Widgets
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.colorResource
import kotlinx.coroutines.CoroutineScope


class RegistrationActivity : ComponentActivity() {
    private val db: DatabaseConnectClass = DatabaseConnectClass(this)
    private val inputSecurity: InputDataSecurity = InputDataSecurity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Constructor()
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
            Widgets.SetImage(R.drawable.small_tree)
            Widgets.WindowTitle("Sign Up")
            SignInForm()
            Widgets.UnderButton(
                "Account exist? Log in!",
                LogInActivity::class.java,
                LocalContext.current
            )
        }
    }


    @Composable
    fun SignInForm() {
        var userLoginInputValue by remember { mutableStateOf("") }
        var userPasswordInputValue by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Widgets.Input(
                "Create login",
                userLoginInputValue,
                OnValueChange = { newText -> userLoginInputValue = newText })

            Widgets.Input(
                "Create password",
                userPasswordInputValue,
                OnValueChange = { newText -> userPasswordInputValue = newText })
            AcceptButton(
                "Create account",
                userLoginInputValue.toString(),
                userPasswordInputValue.toString(),
                LocalContext.current
            )
        }
    }



    @Composable
    fun AcceptButton(
        buttonName: String,
        userLogin: String,
        userPassword: String,
        context: Context // Требуется для переходя в другое окно
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    stringResource(R.string.accept_button_padding).toInt().dp
                    ),
            shape = RectangleShape,

            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_color)),


            onClick = {
                if (inputSecurity.ReportSQLI(listOf(userLogin, userPassword))) {
                    if (db.getNewAccountExistStatus(userLogin, userPassword)) {
                        // Проверка наличия аккаунта у пользователя
                        println("Account ready to create")

                        // Отправка аккаунта на регистрацию
                        db.createUserAccount(userLogin, userPassword)

                        // Открытие главного окна
                        context.startActivity(
                            Intent(
                                context,
                                MainPage::class.java
                            )
                        )

                    } else {
                        Widgets.callSnackBar(
                            "These data already using!",
                            snackbarHostState,
                            scope
                        )
                    }
                } else {
                    Widgets.callSnackBar(
                        "Please input some data!",
                        snackbarHostState,
                        scope
                    )
                }
            }
        ) {
            Text(
                text = buttonName,
                color = Color.White,
                fontSize = stringResource(R.string.button_text_size).toInt().sp
            )
        }

        Widgets.ErrorSnackbar(
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
