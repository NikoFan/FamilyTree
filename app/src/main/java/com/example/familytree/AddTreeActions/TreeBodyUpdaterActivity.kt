package com.example.familytree.AddTreeActions

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.familytree.AddTreeActions.ui.theme.FamilyTreeTheme
import com.example.familytree.Database.DatabaseConnectClass
import com.example.familytree.MainPage
import com.example.familytree.R
import com.example.familytree.Widgets
import com.example.familytree.WorkWithJSON.JsonReader

class TreeBodyUpdaterActivity : ComponentActivity() {

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
            SetPersonIconByHisGender()
            SetPersonNameTitle()
            Widgets.UnderButton(
                "Back",
                TreesViewActivity::class.java,
                LocalContext.current
            )

        }
    }

    @Composable
    fun SetPersonIconByHisGender(){
        println("WORK")
        val db: DatabaseConnectClass = DatabaseConnectClass(this)
        var iconIdNumber: Int = R.drawable.woman_person_icon
        val treeBodyJSON = db.getTreeBody()

        println("WORK2")

        if (JsonReader.GetActivePersonGender(
            treeBodyJSONString = treeBodyJSON
        ).equals("Male")){
            iconIdNumber = R.drawable.man_person_icon
        }

        Widgets.SetImage(iconIdNumber)
    }

    @Composable
    fun SetPersonNameTitle(){
        println("WORK")
        val db: DatabaseConnectClass = DatabaseConnectClass(this)
        Widgets.WindowTitle(JsonReader.GetActivePersonName(treeBodyJSONString = db.getTreeBody()))
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        FamilyTreeTheme {
            Constructor()
        }
    }
}


