package com.team12.ElSpar.ui

import android.preference.Preference
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation

@Composable
fun NavBar(navController: NavHostController){
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            IconButton(onClick = { navController.navigate("InformationScreen")}) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List Icon")
            }
            IconButton(onClick = { navController.navigate("ElSparScreen")}) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon")
            }
            IconButton(onClick = { navController.navigate("SettingsScreen")}) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon"
                )
            }

        }
    }
}

@Composable
fun ElSparApp(
    elSparViewModel: ElSparViewModel,
    modifier: Modifier = Modifier
) {
    val elSparUiState: ElSparUiState
    by elSparViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { },
        bottomBar = {
            if (elSparUiState !is ElSparUiState.SelectArea) NavBar(navController)
        }
    ) { padding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "ElSparScreen") {
                composable("InformationScreen") {
                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel) {
                        InformationScreen(priceList = it.priceList)
                }
            }

                composable("ElSparScreen") {
                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel) {
                        MainScreen(
                            priceList = it.priceList,
                            currentPricePeriod = it.currentPricePeriod,
                            currentEndDate = it.currentEndDate,
                            onChangePricePeriod = { elSparViewModel.updatePricePeriod(it) },
                            onDateForward = { elSparViewModel.dateForward() },
                            onDateBack = { elSparViewModel.dateBack() },
                            modifier = modifier
                        )
                    }
                }

                composable("SettingsScreen") {
                        SettingsScreen(
                            onChangePreferences = { navController.navigate("PreferenceScreen")},
                            onChangePrisomraade  = {navController.navigate("SelectAreaScreen")},
                            onChangeMoms  = {},
                            onChangeInfo  = {navController.navigate("InfoScreen")},
                            onChangeAboutUs  = {navController.navigate("AboutUsScreen")},
                        )
                    }
                composable("PreferenceScreen"){
                    PreferenceScreen()
                }
                composable("SelectAreaScreen"){
                    //SelectAreaScreen()
                }
                composable("InfoScreen"){
                    InfoScreen()
                }
                composable("AboutUsScreen"){
                    AboutUsScreen()
                }
                }
                /*ElSparScreen(
                    priceList = priceList,
                    currentPricePeriod = currentPricePeriod,
                    currentPriceArea = currentPriceArea,
                    currentEndDate = currentEndDate,
                    onChangePricePeriod = { elSparViewModel.updatePricePeriod(it) },
                    onChangePriceArea = { elSparViewModel.updatePriceArea(it) },
                    onDateForward = { elSparViewModel.dateForward() },
                    onDateBack = { elSparViewModel.dateBack() },
                    modifier = modifier
                )*/
        }
    }
}

@Composable
fun DataContent(
    elSparUiState: ElSparUiState,
    elSparViewModel: ElSparViewModel,
    modifier: Modifier = Modifier,
    onSuccessfulLoadContent: @Composable (ElSparUiState.Success) -> Unit
) {
    when (elSparUiState) {
        is ElSparUiState.SelectArea ->
                SelectAreaScreen(
                    currentPriceArea = elSparUiState.currentPriceArea,
                    onChangePriceArea = { elSparViewModel.updatePriceArea(it) }
                )
        is ElSparUiState.Loading -> LoadingScreen(modifier)
        is ElSparUiState.Error -> ErrorScreen(modifier)
        is ElSparUiState.Success -> onSuccessfulLoadContent(elSparUiState)
    }
}


@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("LOADING")
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("ERROR")
    }
}