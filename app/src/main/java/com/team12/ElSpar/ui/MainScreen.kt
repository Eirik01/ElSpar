package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun MainScreen(
    priceList: Map<LocalDateTime, Double>,
    currentPricePeriod: PricePeriod,
    currentPriceArea: PriceArea,
    currentEndDate: LocalDate,
    onChangePricePeriod: (PricePeriod) -> Unit,
    onChangePriceArea: (PriceArea) -> Unit,
    onDateForward: () -> Unit,
    onDateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxHeight().verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        //SJERM INNHOLD!

        //Kort på toppen
        Card_CurrentPrice(priceList)

        //Tidsintervall-knapper
        TimeIntervalButtons(currentPricePeriod) { onChangePricePeriod(it) }

        PriceText(priceList,currentPricePeriod)

    }
}


@Composable
fun Card_CurrentPrice(
    priceList: Map<LocalDateTime, Double>
    ){

    val currPrice = priceList
        .filterKeys { it.toLocalDate() == LocalDate.now() && it.hour == LocalDateTime.now().hour }
        .values
        .first()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ){
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy((-10).dp)
        ){
            Row(horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information icon"
                )
                Text(text = "Strømpris nå")
            }

            Text(text =roundOffDecimal(currPrice).toString(), fontSize = 50.sp, fontWeight = FontWeight.Bold)
            Text(text ="øre/kWh")
        }
    }
}

@Composable
fun TimeIntervalButtons(
    currentPricePeriod: PricePeriod,
    onSelectPricePeriod: (PricePeriod) -> Unit
){

    //Gi weight på samme måte som boksene i infoscreen
    Row(modifier = Modifier
        .height(40.dp)
        .fillMaxWidth()
    ){
        IntervalButton(Modifier.weight(1f),40, 0, currentPricePeriod, PricePeriod.DAY) { onSelectPricePeriod(it) }
        IntervalButton(Modifier.weight(1f),0, 0, currentPricePeriod, PricePeriod.WEEK) { onSelectPricePeriod(it) }
        IntervalButton(Modifier.weight(1f),0, 40, currentPricePeriod, PricePeriod.MONTH) { onSelectPricePeriod(it) }
    }
}
@Composable
fun IntervalButton(
    modifier: Modifier,
    leftRound:Int = 0,
    rightRound:Int = 0,
    currentPricePeriod: PricePeriod,
    btnPricePeriod: PricePeriod,
    onSelectPricePeriod: (PricePeriod) -> Unit)
{
    val unselectedColor = MaterialTheme.colorScheme.background
    val selectedColor = MaterialTheme.colorScheme.primaryContainer
    OutlinedButton(
        onClick = {onSelectPricePeriod(btnPricePeriod)},
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor =  (
                    if (currentPricePeriod == btnPricePeriod) selectedColor else unselectedColor
                    )
        ),
        shape = RoundedCornerShape(
            topStartPercent = leftRound,
            topEndPercent = rightRound,
            bottomEndPercent = rightRound,
            bottomStartPercent = leftRound
        )
    ) {
        Text(text = btnPricePeriod.text, color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
}
@Composable
fun PriceText(
    priceList: Map<LocalDateTime, Double>,
    pricePeriod: PricePeriod,

    ) {



    val avgPrice = priceList.values.average()
    val minPrice = priceList.values.min()
    val maxPrice = priceList.values.max()

    val timeOf: (Double) -> String = { price ->
        priceList
            .filterValues { it == price }
            .keys
            .first()
            .run {
                if (pricePeriod == PricePeriod.DAY) "$hour:00"
                else "$dayOfMonth.$monthValue $hour:00"
            }
    }

    val timeOfPlusOne: (Double) -> String = { price ->
        priceList
            .filterValues { it == price }
            .keys
            .first()
            .run {
                "${hour+1}:00"
            }
    }

    val rowMod:Modifier = Modifier.fillMaxWidth()
    Row(rowMod, horizontalArrangement = Arrangement.SpaceBetween){
        Text("Laveste: ${timeOf(minPrice)} - ${timeOfPlusOne(minPrice)}")
        Text(roundOffDecimal(minPrice).toString() + " kWh")
    }

    Divider(modifier = Modifier.fillMaxWidth(0.9f).width(1.dp))

    Row(rowMod, horizontalArrangement = Arrangement.SpaceBetween){
        Text("Høyeste: ${timeOf(maxPrice)} - ${timeOfPlusOne(maxPrice)}")
        Text(roundOffDecimal(maxPrice).toString() + " kWh")
    }

    Divider(modifier = Modifier.fillMaxWidth(0.9f).width(1.dp))

    Row(rowMod, horizontalArrangement = Arrangement.SpaceBetween){
        Text("Gjennomsnittlig pris:")
        Text(roundOffDecimal(avgPrice).toString() + " kWh")
    }
}

fun roundOffDecimal(number: Double): Double {
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toDouble()
}
