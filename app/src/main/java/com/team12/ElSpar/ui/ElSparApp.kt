package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team12.ElSpar.R
import com.team12.ElSpar.Settings
import com.team12.ElSpar.ui.viewmodel.ElSparUiState
import com.team12.ElSpar.ui.viewmodel.ElSparViewModel
import com.team12.ElSpar.ui.views.LoadingScreen
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
    var currentScreen by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {

            if (elSparUiState !is ElSparUiState.SelectArea) {TopBar(currentScreen, false, navController)}

            when (currentScreen) {
                "Velg prisområde" -> TopBar(currentScreen, true, navController)
                "Preferanser" -> TopBar(currentScreen, true, navController)
                "Mer om strøm" -> TopBar(currentScreen, true, navController)
                "Om oss"-> TopBar(currentScreen, true, navController)
            }

                 },
        bottomBar = { if (elSparUiState !is ElSparUiState.SelectArea) NavBar(navController) }
    ) { padding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = navController, startDestination = "ElSparScreen") {
                composable("ActivitiesScreen") {
                    currentScreen = "Strømkalkulator"

                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel) {
                        ActivitiesScreen(
                            currentPrice = it.currentPrice,
                            shower = settings.shower,
                            wash = settings.wash,
                            oven = settings.oven,
                            car = settings.car,
                            navController = navController
                        )
                    }
                }

                composable("ElSparScreen") {
                    currentScreen = "Strømoversikt"

                    DataContent(
                        elSparUiState = elSparUiState,
                        elSparViewModel = elSparViewModel
                    ) {
                        MainScreen(
                            currentPrice = it.currentPrice,
                            priceList = it.priceList,
                            currentPricePeriod = it.currentPricePeriod,
                            currentEndDate = it.currentEndDate,
                            onChangePricePeriod = { elSparViewModel.updatePricePeriod(it) },
                            onDateForward = { elSparViewModel.dateForward() },
                            onDateBack = { elSparViewModel.dateBack() },
                            modifier = modifier,
                            navController
                        )
                    }
                }

                composable("SettingsScreen") {
                    currentScreen = "Instillinger"

                    SettingsScreen(
                        onChangePreferences = {navController.navigate("PreferenceScreen")},
                        onChangePrisomraade  = {navController.navigate("SelectAreaScreen")},
                        onChangeMoms  = {},
                        onChangeInfo  = {navController.navigate("InfoScreen")},
                        onChangeAboutUs  = {navController.navigate("AboutUsScreen")},
                    )
                }
                composable("PreferenceScreen"){
                    currentScreen = "Preferanser"

                    PreferenceScreen(
                        shower = settings.shower,
                        wash = settings.wash,
                        oven = settings.oven,
                        car = settings.car,
                        onUpdatedPreference = { activity, value ->
                            elSparViewModel.updatePreference(activity, value)
                        }
                    )
                }
                composable("SelectAreaScreen"){
                    currentScreen = "Velg prisområde"
                    SelectAreaScreen(
                        currentPriceArea = settings.area,
                        onChangePriceArea = {
                            elSparViewModel.updatePreference(it)
                        }
                    )
                }
                composable("InfoScreen"){
                    currentScreen = "Mer om strøm"
                    InfoScreen()
                }
                composable("AboutUsScreen"){
                    currentScreen = "Om oss"
                    AboutUsScreen()
                }
            }
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
                    onChangePriceArea = {
                        elSparViewModel.updatePreference(it)
                    }
                )
        is ElSparUiState.Loading -> LoadingScreen(modifier)
        is ElSparUiState.Error -> ErrorScreen(modifier)
        is ElSparUiState.Success -> onSuccessfulLoadContent(elSparUiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(currScreen: String, button: Boolean, navController: NavHostController){
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(
                onClick = { navController.navigate("SettingsScreen")},
                enabled = button,
                colors = IconButtonDefaults.iconButtonColors(
                    disabledContentColor = Color.Black.copy(alpha = 0.0f))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "ArrowBack Icon"
                )
            }
        },
        title = { Text(text = currScreen, color = MaterialTheme.colorScheme.onPrimaryContainer) },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun NavBar(navController: NavHostController){
    BottomAppBar(
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            IconButton(onClick = { navController.navigate("ActivitiesScreen")}) {
                Icon(
                    painter = painterResource(id = R.drawable.calculatesmall),
                    contentDescription = "Calculate Icon")
            }
            IconButton(onClick = { navController.navigate("ElSparScreen")}) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home Icon")
            }
            IconButton(onClick = { navController.navigate("SettingsScreen")}) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List Icon"
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