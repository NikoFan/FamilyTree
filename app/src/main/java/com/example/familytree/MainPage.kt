package com.example.familytree

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familytree.AddTreeActions.AddTreeActivity
import com.example.familytree.AddTreeActions.TreesViewActivity
import com.example.familytree.Widgets.acceptValue
import com.example.familytree.ui.theme.FamilyTreeTheme

class MainPage : ComponentActivity() {
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
            Widgets.SetImage(R.drawable.main_window)
            Widgets.WindowTitle("Home")
            Widgets.InstructionText("Choose your next step")
            ButtonsForm()
        }
    }

    @Composable
    fun ButtonsForm() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(stringResource(R.string.accept_button_padding).toInt().dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SwitchButton(
                "Your Trees",
                TreesViewActivity::class.java,
                LocalContext.current
            )

            SwitchButton(
                "Plant a Tree",
                AddTreeActivity::class.java,
                LocalContext.current
            )

            SwitchButton(
                "About us",
                RegistrationActivity::class.java,
                LocalContext.current
            )

            Widgets.UnderButton(
                buttonName = "Log out",
                nextClassActivity = LogInActivity::class.java,
                context = LocalContext.current
            )
        }
    }

    @Composable
    fun SwitchButton(
        buttonContent: String,
        buttonGoalClass: Class<out Activity>,
        context: Context = LocalContext.current
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                context.startActivity(
                    Intent(
                        context,
                        buttonGoalClass
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.button_color)
            ),
            shape = RectangleShape
        )
        {
            Text(
                text = buttonContent,
                color = Color.White,
                fontSize = stringResource(R.string.under_button_text_size).toInt().sp

            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FamilyTreeTheme {
            Constructor()
        }
    }
}

