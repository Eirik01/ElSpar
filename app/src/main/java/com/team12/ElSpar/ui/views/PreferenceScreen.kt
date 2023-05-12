package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.Settings

data class Activity(
    val type: Settings.Activity,
    val preference: Int,
    val title: String,
    val unit: String,
)

@Composable
fun PreferenceScreen(
    shower: Int,
    wash: Int,
    oven: Int,
    car: Int,
    onUpdatedPreference: (Settings.Activity, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(1f)
            .verticalScroll(rememberScrollState())
    ){
        val activities = listOf(
            Activity(Settings.Activity.SHOWER, shower,"Dusj" ,"minutter"),
            Activity(Settings.Activity.WASH, wash,"Klesvask" , "minutter" ),
            Activity(Settings.Activity.OVEN, oven,"Ovn" , "minutter" ),
            Activity(Settings.Activity.CAR, car,"Lade bil" , "KWh"))
        Card(
            modifier = modifier
                .padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer)
        ){
            activities.forEach{
                sliderElement(
                    it,
                    onUpdatedPreference
                )
            }
        }
        Spacer(modifier = modifier.size(15.dp))
        Text(
            text = "Her velger du hvor lenge aktivitetene i ´Strømkalkulator´-skjermen varer. For å lade" +
                    "bil regner vi med en fullading, og derfor velger du istedet kapasiteten til elbilen" +
                    "din sitt batteri.",
            textAlign = TextAlign.Justify,
            modifier = modifier
                .padding(20.dp)
        )
        Text(
            text = "Antagelser har blitt gjort for de forskjellige aktiviteterna. Vi har antatt at:\n" +
                    "  - En dusj bruker runt 5kWh\n" +
                    "  - En klesvask bruker runt 0.4kWh\n" +
                    "  - En ovn bruker runt 3.5kWh\n",
            textAlign = TextAlign.Justify,
            modifier = modifier
                .padding(20.dp)
        )
    }
}

@Composable
fun sliderElement(
    activity : Activity,
   onUpdatedPreference: (Settings.Activity, Int) -> Unit,
   modifier: Modifier = Modifier
){
    var preference by remember { mutableStateOf(activity.preference) }
    Text(
        text = "${activity.title}   $preference  ${activity.unit} ",
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



