package com.team12.ElSpar.ui

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
        topBar = {
            //TODO
        },
        bottomBar = {
            //Legger navbar her ettersom den skal på alle skjermene
            NavBar(navController);
        }
    ) { padding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            when (elSparUiState) {
                is ElSparUiState.Loading -> LoadingScreen(modifier)
                is ElSparUiState.Error -> ErrorScreen(modifier)
                is ElSparUiState.Success ->
                (elSparUiState as ElSparUiState.Success).let { currentState ->

                    NavHost(navController = navController, startDestination = "ElSparScreen") {
                        composable("InformationScreen") {
                            InformationScreen()
                        }
                        composable("ElSparScreen") {
                            ElSparScreen(
                                priceList = currentState.priceList,
                                currentPricePeriod = currentState.currentPricePeriod,
                                onChangePricePeriod = { elSparViewModel.updatePricePeriod(it) },
                                onUpdatePriceArea = {elSparViewModel.updatePriceArea(it)},
                                modifier = modifier
                            )
                        }
                        composable("SettingsScreen") {
                            SettingsScreen()
                        }
                    }
                    //ShowMainScreen();

                }
            }
        }
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