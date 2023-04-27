package com.team12.ElSpar.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun SettingsScreen(
    onChangePreferences :  () -> Unit,
    onChangePrisomraade :  () -> Unit,
    onChangeMoms :  () -> Unit,
    onChangeInfo :  () -> Unit,
    onChangeAboutUs :  () -> Unit,
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
        val settingCardsTitles  = listOf(stringResource(R.string.Preferanser), stringResource(R.string.velg_prisområde),
            stringResource(R.string.skru_av_moms), stringResource(R.string.informasjon), stringResource(R.string.om_oss))
        LazyColumn(
            modifier = Modifier.padding(top = 60.dp)
        ) {
            items(settingCardsTitles.size) { index ->
                var onChangeFunction  : () -> Unit = {}
                when (settingCardsTitles[index]){
                    "Preferanser" -> onChangeFunction = onChangePreferences
                    "Velg prisområde" -> onChangeFunction = onChangePrisomraade
                    "Skru av moms" -> onChangeFunction = onChangeMoms
                    "Informasjon" -> onChangeFunction = onChangeInfo
                    "Om oss" -> onChangeFunction = onChangeAboutUs
                }
                Card(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .padding(4.dp)
                        .height(60.dp)
                        .fillMaxWidth()
                        .clickable(onClick = { onChangeFunction() }

                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,

                        ) {
                        Text(
                            text = settingCardsTitles[index],
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

