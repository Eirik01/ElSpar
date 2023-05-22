package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.R
import java.time.LocalDateTime
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.team12.ElSpar.ui.chart.AveragePriceEntry
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.ceil

@Composable
fun ActivitiesScreen(
    currentPrice: Map<LocalDateTime, Double>,
    shower: Int,
    wash: Int,
    oven: Int,
    car: Int,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        // here is the price right now instantiatied
        val priceNow by remember {
            mutableStateOf(            currentPrice
                .filterKeys { it.hour == LocalDateTime.now().hour }
                .values
                .first())
        }
        // Here is the ratio betwwen the average price of today and price right now calculated
        val priceRatio = priceNow/currentPrice.values.average()
        var priceRatioDiff = 0.0
        var cheaper : Boolean = false
        if(priceRatio < 1){
            //cheaper than average
            priceRatioDiff = 1-priceRatio
            cheaper = true
        }else{
            //pricier than average
            priceRatioDiff -= 1
        }
        // all composables that are shown below
        Card_CurrentPrice(currentPrice, navController)
        ContentRow(
            priceRatioDiff,
            cheaper,
            currentPrice
                .filterKeys { it.hour == LocalDateTime.now().hour }
                .values
                .first(),
            "$shower min dusj",
            shower,
            "$wash min klesvask",
            wash,
            R.drawable.shower,
            R.drawable.laundry,
            navController = navController
        )
        ContentRow(
            priceRatioDiff,
            cheaper,
            currentPrice
                .filterKeys { it.hour == LocalDateTime.now().hour }
                .values
                .first(),
            "Ovn ($oven min)",
            oven,
            "Lade bil ($car kWh)",
            car,
            R.drawable.oven,
            R.drawable.charger,
            navController = navController
        )
        //Tekst på bunnen av siden
        //ENDRE DISSE! VARIABLENE
        val formattedPriceRatioDiff = (priceRatioDiff*100).toBigDecimal().setScale(1, RoundingMode.CEILING)
        var textString = "Strømprisen nå er $formattedPriceRatioDiff% " + (if(!cheaper) "over" else "under") + " dagens gjennomsnitt. "
        textString += if(cheaper){
            "Det er ikke en dårlig ide å bruke mye strøm nå."
        } else{
            "Det er lurt å vente med strømintense oppgaver."
        }

        Text(
            text = textString,
            textAlign = TextAlign.Justify,
            modifier = modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = modifier.size(15.dp))

        Text(
            "Kortene på siden viser pris for aktivitet, og hvor mye dyrere eller billigere det er å gjøre aktiviteten nå i forhold til de siste 24 timene sitt gjennomsnitt",
            textAlign = TextAlign.Justify,
            modifier = modifier.fillMaxWidth(0.8f)
        )
    }
}
@Composable
fun ContentRow(
    activityRatio : Double,
    cheaper : Boolean,
    currentPrice : Double,
    leftActivity : String,
    leftPreference : Int,
    rightActivity : String,
    rightPreference : Int,
    icon1 : Int,
    icon2 : Int,
    modifier : Modifier = Modifier,
    navController: NavHostController
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        ContentCard(
            currentPrice,
            cheaper,
            activityRatio,
            leftActivity,
            leftPreference,
            icon1,
            Modifier.weight(1f),
            navController
        )
        ContentCard(
            currentPrice,
            cheaper,
            activityRatio,
            rightActivity,
            rightPreference,
            icon2,
            Modifier.weight(1f),
            navController
        )
    }
}

@Composable
fun ContentCard(
    currentPrice: Double,
    cheaper : Boolean,
    priceRatio : Double,
    activity : String,
    activityPreference : Int,
    icon : Int,
    modifier : Modifier = Modifier,
    navController: NavHostController
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier.clickable { navController.navigate("PreferenceScreen") }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            var activityKwhCost by remember{ mutableStateOf(0.0) }
            var activityPrice by remember { mutableStateOf(0.0) }
            var activityPriceDiff by remember { mutableStateOf(0.0)}
            if(activity.contains("dusj")){
                activityKwhCost = 5.0
            }
            else if(activity.contains("klesvask")){
                activityKwhCost = 0.4
            }
            else if(activity.contains("Ovn")){
                activityKwhCost = 3.5
            }
            else if(activity.contains("Lade")){
                activityKwhCost = 1.0
            }

            if(activityKwhCost != 1.0){
                activityPrice = currentPrice*activityKwhCost*activityPreference/6000
            }else{
                activityPrice = currentPrice*activityKwhCost*activityPreference/100
            }
            // Divided by 6000 becuase the the price should be shown in kr not in øre, also because preferences are in minutes so (60*100)
            var priceRatioDiff = priceRatio

            activityPriceDiff = priceRatioDiff*activityPrice
            Image(
                painter = painterResource(id = icon),
                contentDescription = "My Image",
                //Modifier.clickable { navController.navigate("PreferenceScreen") }
            )
            Text(text = activity, textAlign = TextAlign.Center)
            //Bottom text-row. Has activity price and price difference
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append(activityPrice.toBigDecimal().setScale(1, RoundingMode.CEILING).toString() + "kr ")
                    }
                    withStyle(style = SpanStyle()) {
                        if(cheaper){
                            append("(" +"-"+ activityPriceDiff.toBigDecimal().setScale(1, RoundingMode.CEILING).toString() + "kr)")
                        }else{
                            append("(" +"+"+activityPriceDiff.toBigDecimal().setScale(1, RoundingMode.CEILING).toString() + "kr)")
                        }
                    }
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
