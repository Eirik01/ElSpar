package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.LocalDateTime

@Composable
fun ElSparApp(
    elSparViewModel: ElSparViewModel,
    modifier: Modifier = Modifier
) {
    val elSparUiState: ElSparUiState
    by elSparViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            //TODO
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
                    ElSparScreen(
                        avgPrice = currentState.priceList.values.average(),
                        maxPrice = currentState.priceList.values.max(),
                        minPrice = currentState.priceList.values.min(),
                        onIntervalChange = { getWeeklyRapport(elSparUiState,elSparViewModel)},
                        modifier = modifier,
                    )
                }
            }
        }
    }
}

fun getWeeklyRapport(
    uiState : ElSparUiState,
    viewModel : ElSparViewModel
    ){
    (uiState as ElSparUiState.Success).let { currentState ->
        uiState.startTime = LocalDateTime.now().minusDays(7)
        viewModel.updateInterval(currentState.startTime,currentState.endTime)
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