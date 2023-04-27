package com.team12.ElSpar.ui



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team12.ElSpar.Settings
import com.team12.ElSpar.ui.viewmodel.ElSparViewModel
import com.team12.ElSpar.ui.viewmodel.ElSparUiState
import com.team12.ElSpar.ui.views.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currScreen: String){
    CenterAlignedTopAppBar(
        title = { Text(text = currScreen)},
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
@Composable
fun ElSparApp(
    elSparViewModel: ElSparViewModel,
    modifier: Modifier = Modifier
) {
    val elSparUiState: ElSparUiState
    by elSparViewModel.uiState.collectAsState()
    val settings: Settings
    by elSparViewModel.settings.collectAsState(Settings.getDefaultInstance())

    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopBar(currentScreen) },
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
                    currentScreen = "Strømkalkulator"
                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel) {
                        ActivitiesScreen(
                            currentPrice = it.currentPrice,
                            showerTime = settings.showerTime
                        )
                    }
                }

                composable("ElSparScreen") {
                    currentScreen = "Strømoversikt"

                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel) {
                        MainScreen(
                            currentPrice = it.currentPrice,
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
                    currentScreen = "Instillninger"
                    SettingsScreen(
                        onChangePreferences = { navController.navigate("PreferenceScreen")},
                        onChangePrisomraade  = {navController.navigate("SelectAreaScreen")},
                        onChangeMoms  = {},
                        onChangeInfo  = {navController.navigate("InfoScreen")},
                        onChangeAboutUs  = {navController.navigate("AboutUsScreen")},
                    )
                }
                composable("PreferenceScreen"){
                    currentScreen = "Preferanser"
                    PreferenceScreen()
                }
                composable("SelectAreaScreen"){

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