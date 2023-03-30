package com.team12.ElSpar.ui

import android.annotation.SuppressLint
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import java.math.RoundingMode
import java.text.DecimalFormat


//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ElSparScreen(
    avgPrice: Double,
    maxPrice: Double,
    minPrice: Double,
    onIntervalChangeWeekly: () -> Unit = {},
    onIntervalChangeMonthly: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    var selectedPriceArea by remember { mutableStateOf("") }
    var selectedTimeInterval = remember { mutableStateOf("") }

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
                        OutlinedTextField(
                            readOnly = true,
                            value = selectedPriceArea,
                            enabled = false,
                            onValueChange = {
                                selectedPriceArea = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFiledSize = coordinates.size.toSize()
                                },
                            label = {Text(text = "Velg prisområde")},
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
                                .width(with(LocalDensity.current){textFiledSize.width.toDp()})
                        ) {
                            list.forEach {label ->
                                DropdownMenuItem(text = {Text(text = label)}, onClick = {
                                    selectedPriceArea = label
                                    expanded = false
                                    when(selectedPriceArea){
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
        //HER GÅR DET SOM ER I "MIDTED" AV SCAFFOLDET
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 10.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            //val unselected
            //sett størrelsen med andre argument (0.5 = 50% av skjermen, 0.3 = 30% etc)
            ScaffoldContent(it, 0.4f, avgPrice, maxPrice, minPrice)
            SwitchButton(selectedTimeInterval, "day", "1 dag", onIntervalChangeWeekly)
            SwitchButton(selectedTimeInterval, "week", "7 dager")
            SwitchButton(selectedTimeInterval, "month", "30 dager")


            //Kan ha grafen her
            //Graph()

        }


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
@Composable
fun SwitchButton(selectedTimeInterval:MutableState<String>,btnTimeInterval:String, btnText:String = "BUTTON", funOnClick: () -> Unit = {}){
    val unselectedColor = MaterialTheme.colorScheme.background;
    val selectedColor = MaterialTheme.colorScheme.primaryContainer;
    OutlinedButton(
        onClick = { funOnClick
            selectedTimeInterval.value = btnTimeInterval
        },
        modifier = Modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor =  (
                    if (selectedTimeInterval.value == btnTimeInterval) selectedColor else unselectedColor
                    )
            )
    ) {
        Text(text = btnText, color = Color.Black)
    }
}
@Composable
fun CardContent(topText:String, midText:String){
    val defModifier = Modifier
        .padding(2.dp)
        .fillMaxSize()
    Column(
        modifier = defModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = topText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Justify
        )
        Row(
            verticalAlignment = Alignment.Bottom
        ){
            Text(
                text = midText,
                fontSize = 36.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Justify
            )
            Text(
                text = "øre/kWh",
                fontSize = 16.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(bottom = 5.dp)
            )
        }

    }
}
@Composable
fun ScaffoldContent(
    padding:PaddingValues,
    height:Float,
    avgPrice: Double,
    maxPrice: Double,
    minPrice: Double,
    modifier: Modifier = Modifier){
val defModifier = Modifier
    .padding(2.dp)
    .fillMaxSize()
    Column(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(padding)
            .fillMaxHeight(height),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        //Kortet på toppen som holder info om nåværende, min og maks
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        ){
            //C1, nåværende
            Box(
                modifier = defModifier.weight(0.6f)
            ) {

                Column(
                    modifier = defModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Row(horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Information icon"
                        )
                        Text(text = "Dagens strømpris",
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "Gjennomsnitt",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        text = roundOffDecimal(avgPrice*100).toString(),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Justify
                    )
                    Text(
                        text = "øre/kWh",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Justify
                    )
                    Divider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .width(1.dp),
                        color = Color.Gray
                    )
                }

            }

            //c2 og c4, laveste og høyeste
            Row(
                modifier = defModifier.weight(0.4f)
            ) {
                Box(
                    modifier = defModifier.weight(0.5f)
                ) {
                    CardContent("Laveste - 12:00", roundOffDecimal(minPrice*100).toString()) //Endre, skal være variabel
                }
                //vertical Divider
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .width(1.dp)
                )
                Box(
                    modifier = defModifier.weight(0.5f)
                ) {
                    CardContent("Høyeste - 16:00", roundOffDecimal(maxPrice*100).toString()) //Endre, skal være variabel

                }

            }

        }
        }

}
fun roundOffDecimal(number: Double): Double? {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ElSparTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ElSparScreen(1.1776,2.2776, 00.0376)
        }
    }
}

