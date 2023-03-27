package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.team12.ElSpar.R

@Composable
fun ElSparScreen(
    avgPrice: Double,
    maxPrice: Double,
    minPrice: Double,
    onIntervalChangeWeekly: () -> Unit,
    onIntervalChangeMonthly: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.avg_price, avgPrice))
        Text(stringResource(R.string.max_price, maxPrice))
        Text(stringResource(R.string.min_price, minPrice))
        Button(
            onClick = onIntervalChangeWeekly,
            content = {
                Row(

                ){
                    Text(text = "see weekly rapport")
                }
            }
        )
        Button(
            onClick = onIntervalChangeMonthly,
            content = {
                Row(

                ){
                    Text(text = "see monthly rapport")
                }
            }
        )
    }
}