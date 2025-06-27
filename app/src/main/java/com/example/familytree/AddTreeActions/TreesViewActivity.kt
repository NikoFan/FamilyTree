package com.example.familytree.AddTreeActions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familytree.AddTreeActions.ui.theme.FamilyTreeTheme
import com.example.familytree.Database.DatabaseConnectClass
import com.example.familytree.MainPage
import com.example.familytree.R
import com.example.familytree.StaticStorage
import com.example.familytree.Widgets


data class TreeCards(
    val tree_id: Int,
    val tree_name: String,
    val tree_icon: Int
)


class TreesViewActivity : ComponentActivity() {

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
            Widgets.WindowTitle("Your trees")
            ProductCardsScreen()
            Widgets.UnderButton(
                "Back",
                MainPage::class.java,
                LocalContext.current
            )

        }
    }

    @Composable
    fun ProductCardsScreen() {
        // Получение списка деревьев у пользователя
        var db: DatabaseConnectClass = DatabaseConnectClass(this)

        var treesList = db.getUserTreesArray()
        var treeCardsArrayList = mutableListOf<Any>()
        for (i in treesList.indices) {
            treeCardsArrayList.add(
                TreeCards(
                    treesList[i][0].toString().toInt(),
                    treesList[i][1].toString(),
                    treesList[i][2].toString().toInt()
                )
            )
        }

        if (treeCardsArrayList.size != 0) {
            val treesCardArray = remember {
                treeCardsArrayList
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
            ) {

                // Область прокрутки с карточками
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(treesCardArray) { treeCardsValue ->
                        FamilyTreeCardItem(
                            treeCardsValue = treeCardsValue as TreeCards,
                            currentTreeId = treeCardsValue.tree_id.toInt(),
                            context = LocalContext.current,
                            db = db
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.7f),
                verticalArrangement = Arrangement.Center
            )
            {
                EmptyCardItem()
            }

        }
    }

    @Composable
    fun EmptyCardItem() {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Black
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        lineHeight = 50.sp,
                        text = "You don't have any trees",
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        fontSize = stringResource(R.string.title_text_size).toInt().sp,
                        color = Color.White
                    )
                }

            }
        }
    }

    @Composable
    fun FamilyTreeCardItem(
        treeCardsValue: TreeCards,
        currentTreeId: Int,
        context: Context,
        db: DatabaseConnectClass
    ) {
        var showDialog by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Blue.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),

                    ) {
                    Widgets.SetImage(treeCardsValue.tree_icon.toInt())

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = treeCardsValue.tree_name,
                        fontSize = stringResource(R.string.title_text_size).toInt().sp,
                        color = colorResource(R.color.button_color)
                    )

                    // UPDATE
                    Button(
                        onClick = {
                            StaticStorage.setTreeId(currentTreeId)
                            if (db.detectIsTreeEmpty()) {
                                context.startActivity(
                                    Intent(
                                        context,
                                        EmptyTreeFirstPersonCreate()::class.java
                                    )
                                )
                            } else {
                                context.startActivity(
                                    Intent(
                                        context,
                                        TreeBodyUpdaterActivity()::class.java
                                    )
                                )
                            }


                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(stringResource(R.string.accept_button_padding).toInt().dp / 3),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.button_color)
                        ),
                        shape = RectangleShape
                    ) {
                        Text(
                            text = "Update",
                            fontSize = stringResource(R.string.button_text_size).toInt().sp,
                            color = Color.White
                        )
                    }
                    // DELETE
                    Button(
                        onClick = {
                            showDialog = true

                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(stringResource(R.string.accept_button_padding).toInt().dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.discard_color)
                        ),
                        shape = RectangleShape

                    ) {
                        Text(
                            text = "Delete",
                            fontSize = stringResource(R.string.button_text_size).toInt().sp,
                            color = Color.White
                        )
                    }
                }
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmation") },
                text = { Text("Are you sure you want to remove the tree?") },
                confirmButton = {
                    Button(
                        onClick = {
                            // Удаление древа из списка
                            db.deleteUserTreeFromTableByIdNumber(
                                currentTreeId
                            )
                            showDialog = false

                            // Переоткрытие окна
                            context.startActivity(
                                Intent(
                                    context,
                                    TreesViewActivity::class.java
                                )
                            )


                        }
                    ) {
                        Text("Yeah")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Constructor()
    }
}
