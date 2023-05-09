package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.unit.toSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.input.KeyboardType
import com.team12.ElSpar.Settings

private data class Activity(
    val type: Settings.Activity,
    val preference: Int,
    val title: String,
    val unit: String,
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreferenceScreen(
    shower: Int,
    wash: Int,
    oven: Int,
    car: Int,
    onUpdatedPreference: (Settings.Activity, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold { padding ->

        val activities = listOf(
            Activity(Settings.Activity.SHOWER, shower,"Dusj" ,"minutter"),
            Activity(Settings.Activity.WASH, wash,"Klesvask" , "minutter" ),
            Activity(Settings.Activity.OVEN, oven,"Ovn" , "minutter" ),
            Activity(Settings.Activity.CAR, car,"Lade bil" , "KWh"))
        Card(
            modifier = modifier
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        ){
            LazyColumn {
                items(activities) { activity ->
                    var preference by remember { mutableStateOf(activity.preference) }
                    Text(
                        text = "${activity.title}(${activity.unit})",
                        modifier = Modifier
                            .padding(top = 15.dp,start = 10.dp),
                        fontSize = 16.sp,
                    )
                    Slider(
                        modifier = modifier.padding(start = 20.dp, end = 20.dp),
                        onValueChange = {
                            preference = it.toInt()
                            onUpdatedPreference(activity.type, it.toInt())
                        },
                        steps = 360,
                        value = preference.toFloat(),
                        valueRange = 1f..360f
                    )
                }
            }
        }
    }
}

