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


@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PreferenceScreen(
    ) {
    Scaffold(){
        padding ->
        var placeHolderPadding by remember { mutableStateOf(0) }
        var textFiledSize by remember { mutableStateOf(Size.Zero) }

        var showerPreference : String by remember { mutableStateOf("10") } // måste fixas
        var washPreference : String by  remember { mutableStateOf("60") } // måste fixas
        var ovenPreference : String by remember { mutableStateOf("15") } // måste fixas
        var carPreference : String by remember { mutableStateOf("24") } // måste fixas

        val settingCardsTitles  : List<Pair<String,String>> = listOf(
            Pair("Dusj" ,"minutter"),
            Pair("Klesvask" , "minutter" ),
            Pair("Ovn" , "minutter" ),
            Pair("Lade bil" , "KWh"))

        LazyColumn() {
            items(settingCardsTitles.size) { index ->
                var shownValue : String = ""
                when(index){
                    0 -> shownValue = showerPreference
                    1 -> shownValue = washPreference
                    2 -> shownValue = ovenPreference
                    3 -> shownValue = carPreference
                }
                OutlinedTextField(
                    value = "",
                    enabled = false,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            textFiledSize = coordinates.size.toSize()
                        },
                    label = {Text(
                        text = settingCardsTitles[index].first+" ("+settingCardsTitles[index].second+")",
                        modifier = Modifier
                            .padding(top = placeHolderPadding.dp),
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
                        when(index){
                            0 -> showerPreference = it
                            1 -> washPreference = it
                            2 -> ovenPreference = it
                            3 -> carPreference = it
                        }
                        //Log.d("it",it) ; Log.d("value",shownValue)
                        Log.d("showerPreference",showerPreference)
                        Log.d("washPreference",washPreference)
                        Log.d("ovenPreference",ovenPreference)
                        Log.d("carPreference",carPreference)
                    },
                    value = shownValue,
                    enabled = true,
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
