package com.team12.ElSpar.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.patrykandpatrick.vico.core.extension.ceil


@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PreferenceScreen(
    modifier : Modifier = Modifier
    ) {
    Scaffold() { padding ->
        var showerPreference: Int by remember { mutableStateOf(10) } // måste fixas
        var washPreference: Int by remember { mutableStateOf(60) } // måste fixas
        var ovenPreference: Int by remember { mutableStateOf(15) } // måste fixas
        var carPreference: Int by remember { mutableStateOf(24) } // måste fixas

        val settingCardsTitles: List<Pair<String, String>> = listOf(
            Pair("Dusj", "minutter"),
            Pair("Klesvask", "minutter"),
            Pair("Ovn", "minutter"),
            Pair("Lade bil", "KWh")
        )
        Card(
            modifier = modifier
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        ){
            LazyColumn() {
                items(settingCardsTitles.size) { index ->
                    var shownValue: Int = 0
                    when (index) {
                        0 -> shownValue = showerPreference
                        1 -> shownValue = washPreference
                        2 -> shownValue = ovenPreference
                        3 -> shownValue = carPreference
                    }


                    Text(
                    text = settingCardsTitles[index].first + " (" + settingCardsTitles[index].second + "):   "+shownValue,
                    modifier = Modifier
                        .padding(top = 15.dp,start = 10.dp),
                    fontSize = 16.sp,
                    )


                    Slider(
                        modifier = modifier.padding(start = 20.dp, end = 20.dp),
                        onValueChange = {
                            when (index) {
                            0 -> showerPreference = it.toInt()
                            1 -> washPreference = it.toInt()
                            2 -> ovenPreference = it.toInt()
                            3 -> carPreference = it.toInt()
                            }
                        },
                        steps = 360,
                        value = shownValue.toFloat(),
                        valueRange = 1f..360f
                    )
                }
            }
        }
    }
}

