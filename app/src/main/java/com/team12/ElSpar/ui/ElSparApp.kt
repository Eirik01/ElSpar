package com.team12.ElSpar.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.team12.ElSpar.ui.viewmodel.MainUiState
import com.team12.ElSpar.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay

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
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val mainUiState: MainUiState
    by mainViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { },
        bottomBar = {
            if (mainUiState !is MainUiState.SelectArea) NavBar(navController)
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
                        mainUiState = mainUiState,
                        mainViewModel = mainViewModel) {
                        InformationScreen(priceList = it.priceList)
                    }
                }

                composable("ElSparScreen") {
                    DataContent(
                        mainUiState = mainUiState,
                        mainViewModel = mainViewModel) {
                        MainScreen(
                            currentPrice = it.currentPrice,
                            priceList = it.priceList,
                            currentPricePeriod = it.currentPricePeriod,
                            currentEndDate = it.currentEndDate,
                            onChangePricePeriod = { mainViewModel.updatePricePeriod(it) },
                            onDateForward = { mainViewModel.dateForward() },
                            onDateBack = { mainViewModel.dateBack() },
                            modifier = modifier
                        )
                    }
                }

                composable("SettingsScreen") {
                        SettingsScreen()
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
    mainUiState: MainUiState,
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onSuccessfulLoadContent: @Composable (MainUiState.Success) -> Unit
) {
    when (mainUiState) {
        is MainUiState.SelectArea ->
                SelectAreaScreen(
                    currentPriceArea = mainUiState.currentPriceArea,
                    onChangePriceArea = { mainViewModel.updatePriceArea(it) }
                )
        is MainUiState.Loading -> LoadingScreen(modifier)
        is MainUiState.Error -> ErrorScreen(modifier)
        is MainUiState.Success -> onSuccessfulLoadContent(mainUiState)
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
        //Text("LOADING")
        LoadingAnimation()
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

@Composable
fun LoadingAnimation(
    circleColor: Color = MaterialTheme.colorScheme.primaryContainer,
    circleSize: Dp = 36.dp,
    animationDelay: Int = 400,
    initialAlpha: Float = 0.3f
) {

    // 3 circles
    val circles = listOf(
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        },
        remember {
            Animatable(initialValue = initialAlpha)
        }
    )

    circles.forEachIndexed { index, animatable ->

        LaunchedEffect(Unit) {

            // Use coroutine delay to sync animations
            delay(timeMillis = (animationDelay / circles.size).toLong() * index)

            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = animationDelay
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    // container for circles
    Row(
        modifier = Modifier
        //.border(width = 2.dp, color = Color.Magenta)
    ) {

        // adding each circle
        circles.forEachIndexed { index, animatable ->

            // gap between the circles
            if (index != 0) {
                Spacer(modifier = Modifier.width(width = 6.dp))
            }

            Box(
                modifier = Modifier
                    .size(size = circleSize)
                    .clip(shape = CircleShape)
                    .background(
                        color = circleColor
                            .copy(alpha = animatable.value)
                    )
            ) {
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    LoadingAnimation()
}