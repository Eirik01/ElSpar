package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun ActivitiesScreen(
    currentPrice: Map<LocalDateTime, Double>,
    shower: Int,
    wash: Int,
    oven: Int,
    car: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxHeight().verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        //ALLE COMPOSABLES SOM SKAL VISES PÅ SKJERMEN GÅR HER vv
        Card_CurrentPrice(currentPrice)
        ContentRow(
            "$shower min dusj", 1.42f, 0.04f,
            "$wash min klesvask", 0.13f, 0.01f
        )
        ContentRow(
            "Ovn ($oven min)", 0.23f, 0.05f,
            "Lade bil ($car kWh)", 5.27f, -1.23f
        )
        //Tekst på bunnen av siden
        //ENDRE DISSE! VARIABLENE
        val hoyereDagspris = true

        var textString = "Strømprisen nå er xx% " + (if(hoyereDagspris) "over" else "under") + " dagens gjennomsnitt. "
        textString += if(!hoyereDagspris){
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
aktivitet1:String, aktivitetPris1:Float, aktivitetPrisDifferanse1:Float,
aktivitet2:String, aktivitetPris2:Float, aktivitetPrisDifferanse2:Float
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        ContentCard(Modifier.weight(1f), aktivitet1, aktivitetPris1, aktivitetPrisDifferanse1)
        ContentCard(Modifier.weight(1f), aktivitet2, aktivitetPris2, aktivitetPrisDifferanse2)
    }
}

@Composable
fun ContentCard(
    modifier: Modifier,
    aktivitet:String,
    aktivitetPris:Float,
    aktivitetPrisDifferanse:Float
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            Image(
                painter = painterResource(id = R.drawable.noimage),
                contentDescription = "My Image"
            )
            Text(text = aktivitet, textAlign = TextAlign.Center)

            //Nederste tekst-rad. Har pris på aktivitet og prisforskjell
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append(aktivitetPris.toString() + "kr ")
                    }
                    withStyle(style = SpanStyle()) {
                        append("(" + aktivitetPrisDifferanse.toString() + "kr)")
                    }
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
