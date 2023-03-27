package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.team12.ElSpar.ui.theme.ElSparTheme


import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElSparScreen(
    avgPrice: Double,
    maxPrice: Double,
    minPrice: Double,
    modifier: Modifier = Modifier
) {
    var selectedItem by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {Column(){
                    //dropdown-meny
                    val list: List<String> = listOf("NO1 – Østlandet","NO2 – Sørlandet","NO3 – Midt-Norge","NO4 – Nord-Norge","NO5 – Vestlandet")
                    var textFiledSize by remember { mutableStateOf(Size.Zero) }

                    Column(
                        modifier = Modifier.padding(20.dp)

                    ) {
                        TextField(

                            readOnly = true,
                            value = selectedItem,
                            onValueChange = {
                                selectedItem = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFiledSize = coordinates.size.toSize()
                                },
                            label = {Text(text = "Velg prisområde")},
                            colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor =  MaterialTheme.colorScheme.primaryContainer, //hide the indicator
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.primaryContainer)
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){textFiledSize.width.toDp()})
                        ) {
                            list.forEach {label ->
                                DropdownMenuItem(text = {Text(text = label)}, onClick = {
                                    selectedItem = label
                                    expanded = false
                                    when(selectedItem){
                                        //Gjør noe basert på valgt område(selected item)
                                        else -> null
                                    }
                                })
                            }
                        }
                    }
                }},
                modifier = Modifier,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Velg prisområde"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Icon(imageVector = Icons.Default.List, contentDescription = "Person Icon")
                    Icon(imageVector = Icons.Default.Home, contentDescription = "Person Icon")
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Person Icon")

                }
            }
        }
    ) {
        Text("Hello world!", modifier = Modifier.padding(it))
    }

    /*
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.avg_price, avgPrice))
        Text(stringResource(R.string.max_price, maxPrice))
        Text(stringResource(R.string.min_price, minPrice))
    }*/
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ElSparTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ElSparScreen(2.0,3.0,1.0)
        }
    }
}