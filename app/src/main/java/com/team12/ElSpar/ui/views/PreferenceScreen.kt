package com.team12.ElSpar.ui

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
import androidx.compose.foundation.lazy.items
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
        var textFieldSize by remember { mutableStateOf(Size.Zero) }

        val activities = listOf(
            Activity(Settings.Activity.SHOWER, shower,"Dusj" ,"minutter"),
            Activity(Settings.Activity.WASH, wash,"Klesvask" , "minutter" ),
            Activity(Settings.Activity.OVEN, oven,"Ovn" , "minutter" ),
            Activity(Settings.Activity.CAR, car,"Lade bil" , "KWh"))

        LazyColumn {
            items(activities) { activity ->
                var preference by remember { mutableStateOf(activity.preference.toString()) }
                OutlinedTextField(
                    value = "",
                    enabled = false,
                    onValueChange = {},
                    modifier = modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFieldSize = coordinates.size.toSize()
                        },
                    label = {Text(
                        text = "${activity.title}(${activity.unit})",
                        modifier = modifier
                            .padding(padding),
                        fontSize = 16.sp,
                        color = Color.Black
                    )},
                    colors = TextFieldDefaults.outlinedTextFieldColors (
                        focusedBorderColor =  MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledTextColor = Color.Black
                    )
                )
                TextField(
                    onValueChange = {
                        preference = it
                        onUpdatedPreference(activity.type, it.toIntOrNull() ?: 0)
                    },
                    value = preference,
                    enabled = true,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier
                        .height(49.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors (
                        focusedBorderColor =  MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledTextColor = Color.Black
                    )
                )
            }
        }
    }
}
