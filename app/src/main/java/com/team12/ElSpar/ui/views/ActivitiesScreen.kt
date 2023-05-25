package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.navigation.NavHostController
import java.math.RoundingMode

//Composable that shows the different activities and their prices
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
    // "price right now" instantiatied
    val priceNow by remember {
        mutableStateOf(            currentPrice
            .filterKeys { it.hour == LocalDateTime.now().hour }
            .values
            .first())
    }
    // Ratio betwwen the average price of today and price right now calculated
    val cheaper : Boolean = (currentPrice.values.average() - priceNow) > 1
    val priceRatio = priceNow/currentPrice.values.average()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        // all composables that are shown below

        //This is the card on top of the screen. It is the same as the one on main screen.
        Card_CurrentPrice(currentPrice, navController)

        //Two "content rows", each with 2 cards.
        ContentRow(
            priceRatio,
            cheaper,
            currentPrice
                .filterKeys { it.hour == LocalDateTime.now().hour }
                .values
                .first(),
            "$shower min dusj",
            shower,
            "$wash min klesvask",
            wash,
            R.drawable.showericon,
            R.drawable.laundry,
            navController = navController
        )
        ContentRow(
            priceRatio,
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

        //Text on the bottom of the side.
        val formattedPriceRatioDiff = (priceRatio).toBigDecimal().setScale(1, RoundingMode.CEILING)
        var textString = "Strømprisen nå er $formattedPriceRatioDiff ganger dagens gjennomsnitt. "
        textString += if(cheaper){
            "Det er ikke en dårlig ide å bruke mye strøm nå."
        } else{
            "Det er lurt å vente med strømintense oppgaver."
        }

        Spacer(modifier = modifier.size(15.dp))

        //Header
        Text(
            text = "Prisen nå",
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth(0.8f),
            fontWeight = Bold
        )

        Row {
            Text(
                text = textString,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth(0.8f)
            )

            //Icon that shows check when the price is low, and warning when the price is high
            if (cheaper) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Check icon",
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning icon",
                )
            }
        }
    }
}

/*Row with 2 content-cards*/
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
        modifier = modifier.fillMaxWidth(),
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

/*Card with an activity. */
@Composable
fun ContentCard(
    currentPrice: Double,
    cheaper : Boolean,
    activityRatio : Double,
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

            //Should do this with a parameter instead
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

            // Divided by 6000 becuase the the price should be shown in kr not in øre, also because preferences are in minutes so (60*100)
            if(activityKwhCost != 1.0){
                activityPrice = currentPrice*activityKwhCost*activityPreference/6000
            }else{
                activityPrice = currentPrice*activityKwhCost*activityPreference/100
            }

            activityPriceDiff = activityRatio*activityPrice

            Image(
                painter = painterResource(id = icon),
                contentDescription = "My Image",
            )
            Text(text = activity, textAlign = TextAlign.Center)

            //Bottom text-row. Has activity price
            Text(
                buildAnnotatedString {
                    //This string is the price
                    withStyle(style = SpanStyle(fontWeight = Bold, fontSize = 20.sp)) {
                        append(activityPrice.toBigDecimal().setScale(1, RoundingMode.CEILING).toString() + "kr ")
                    }
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
