package com.team12.ElSpar.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SettingsScreen(
){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Text("Innstillinger")},
                modifier = Modifier
            )
        },

        ){
            padding ->
        val settingCardsTitles  = listOf("Din Pappa","Din Pappa2","Din Pappa3","a","b","c","d","e","f")
        LazyColumn(
            modifier = Modifier.padding(top = 60.dp)
        ) {
            items(settingCardsTitles.size) { index ->
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(60.dp)
                        .fillMaxWidth()
                        .clickable(onClick = { Log.d("Click_in_Settings","Clicked ${settingCardsTitles[index]} function")}),

                    ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,

                        ) {
                        Text(
                            text = settingCardsTitles[index],
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(text : String){
    return Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        content = {
            Text(text)
        }
    )
}