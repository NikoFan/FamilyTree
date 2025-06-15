package com.example.familytree.AddTreeActions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
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
import com.example.familytree.MainPage
import com.example.familytree.R
import com.example.familytree.Widgets

class AddTreeActivity : ComponentActivity() {
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
            Widgets.SetImage(R.drawable.create_tree)
            Widgets.WindowTitle("Plant a new tree")
            Widgets.InstructionText("Enter tree's name")
            TreesNameForm()
            Widgets.UnderButton(
                "Next time",
                MainPage::class.java,
                LocalContext.current
            )

        }
    }

    @Composable
    fun TreesNameForm(){
        var treeInputName by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Widgets.Input(
                labelText = "Tree's name",
                value = treeInputName,
                OnValueChange = {newText -> treeInputName = newText}
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(stringResource(R.string.accept_button_padding).toInt().dp),
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button_color)),
                onClick = {
                    var treeName: String = treeInputName.toString()
                    if (treeName.length != 0){
                        println("Имя подходит")
                    } else{
                        Widgets.callSnackBar(
                            "Enter some text",
                            snackbarHostState,
                            scope
                        )
                    }
                }
            ) {
                Text(
                    text = "To Plant",
                    color = Color.White,
                    fontSize = stringResource(R.string.button_text_size).toInt().sp
                )
            }
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

