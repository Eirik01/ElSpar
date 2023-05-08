package com.team12.ElSpar.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.R

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SettingsScreen(
    onChangePreferences :  () -> Unit,
    onChangePrisomraade :  () -> Unit,
    onChangeMoms :  () -> Unit,
    onChangeInfo :  () -> Unit,
    onChangeAboutUs :  () -> Unit,
    modifier : Modifier = Modifier,
){
    Scaffold(){
            padding ->
        val settingCardsTitles  = listOf(
            stringResource(R.string.skru_av_moms),
            stringResource(R.string.Preferanser),
            stringResource(R.string.velg_prisområde),
            stringResource(R.string.mer_om_strom),
            stringResource(R.string.om_oss))
        LazyColumn(
        ) {
            items(settingCardsTitles.size) { index ->
                var onChangeFunction  : () -> Unit = {}
                when (settingCardsTitles[index]){
                    "Skru av moms" -> onChangeFunction = onChangeMoms
                    "Preferanser" -> onChangeFunction = onChangePreferences
                    "Velg prisområde" -> onChangeFunction = onChangePrisomraade
                    "Mer om strøm" -> onChangeFunction = onChangeInfo
                    "Om oss" -> onChangeFunction = onChangeAboutUs
                }
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 4.dp, end = 4.dp)
                        .height(60.dp)
                        .fillMaxWidth()
                        .clickable(onClick = { onChangeFunction() }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        Text(
                            text = settingCardsTitles[index],
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        if(index == 0){
                            var momsEnabled by remember { mutableStateOf(false)}
                            Switch(
                                modifier = modifier
                                    .padding(start = 175.dp),
                                checked = momsEnabled ,
                                onCheckedChange = {
                                    momsEnabled = it
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

