package com.team12.ElSpar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.team12.ElSpar.Settings.PriceArea
import com.team12.ElSpar.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAreaScreen(
    currentPriceArea: PriceArea,
    onChangePriceArea: (PriceArea) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    var placeHolderPadding by remember { mutableStateOf(0) }
    val maxPlaceHolderPadding = 7
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Column{
                    //dropdown-meny
                    var textFiledSize by remember { mutableStateOf(Size.Zero) }

                    Column(
                        modifier = modifier.padding(20.dp)
                    ) {
                        OutlinedTextField(
                            readOnly = true,
                            value = currentPriceArea.name,
                            enabled = false,
                            onValueChange = {},
                            modifier = modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFiledSize = coordinates.size.toSize()
                                },
                            label = {Text(
                                text = stringResource(R.string.pick_price_area),
                                modifier = Modifier.padding(top = placeHolderPadding.dp))},
                            colors = TextFieldDefaults.outlinedTextFieldColors (
                                focusedBorderColor =  MaterialTheme.colorScheme.primaryContainer, //hide the indicator
                                unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledBorderColor = MaterialTheme.colorScheme.primaryContainer,
                                disabledTextColor = Color.Black
                            )
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current){ textFiledSize.width.toDp() })
                        ) {
                            PriceArea.values().dropLast(1).forEach {
                                DropdownMenuItem(
                                    text = {Text(text = it.name)},
                                    onClick = {
                                        expanded = false
                                        placeHolderPadding = maxPlaceHolderPadding
                                        onChangePriceArea(it)
                                    }
                                )
                            }
                        }
                    }
                }
                },
                modifier = modifier,
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = icon,
                            contentDescription = stringResource(R.string.pick_price_area)
                        )
                    }
                },
            )
        }
    ) { padding ->
        //HER GÅR DET SOM ER I "MIDTED" AV SCAFFOLDET
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 10.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            //Plass mellom hvert element
            verticalArrangement = Arrangement.spacedBy(10.dp)

        ) {


            image()

        }
    }
}

@Composable
fun image() {
    val imageModifier = Modifier.fillMaxSize()
    Image(
        painter = painterResource(id = R.drawable.prisomrader),
        contentDescription = stringResource(id = R.string.prisomraderPlaceholder),
        contentScale = ContentScale.Fit,
        modifier = imageModifier
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewLandingScreen() {
    fun updatePriceArea(v: PriceArea) {}
    SelectAreaScreen(
        currentPriceArea = PriceArea.NO1,
        onChangePriceArea = {updatePriceArea(it)},
    )
}

