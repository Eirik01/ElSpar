package com.team12.ElSpar.ui

import android.media.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.R
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.ui.theme.ElSparTheme
import java.time.LocalDate
import java.time.LocalDateTime
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier
            .fillMaxHeight().verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){

        //ALLE COMPOSABLES SOM SKAL VISES PÅ SKJERMEN GÅR HER vv
        Card_CurrentPrice();
        ContentRow(
            "10min dusj", 1.42f, 0.04f,
            "Klesvask", 0.13f, 0.01f
        );
        ContentRow(
            "Ovn (30min)", 0.23f, 0.05f,
            "Lade bil (30kWh)", 5.27f, -1.23f
        );


        //Tekst på bunnen av siden
        //ENDRE DISSE! VARIABLENE
        val hoyereDagspris:Boolean = true;

        var textString = "Strømprisen nå er xx% " + (if(hoyereDagspris) "over" else "under") + " dagens gjennomsnitt. "
        textString += if(!hoyereDagspris){
            "Det er ikke en dårlig ide å bruke mye strøm nå."
        } else{
            "Det er lurt å vente med strømintense oppgaver."
        }

        Text(
            text = textString,
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.size(15.dp))

        Text(
            "Kortene på siden viser pris for aktivitet, og hvor mye dyrere eller billigere det er å gjøre aktiviteten nå i forhold til de siste 24 timene sitt gjennomsnitt",
            textAlign = TextAlign.Justify,
            modifier = Modifier.fillMaxWidth(0.8f)
            )

    }
}

@Composable
fun Card_CurrentPrice(){
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
                Text(text = "Dagens strømpris")
            }

            Text(text ="Nå")
            Text(text ="80", fontSize = 50.sp, fontWeight = FontWeight.Bold)
            Text(text ="øre/kWh")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefPreview(){
    ElSparTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){

                //ALLE COMPOSABLES SOM SKAL VISES PÅ SKJERMEN GÅR HER vv
                Card_CurrentPrice();
                ContentRow(
                    "10min dusj", 1.42f, 0.04f,
                    "Klesvask", 0.13f, 0.01f
                        );

                ContentRow(
                    "Steke pizza", 0.23f, 0.05f,
                    "Lade bil (30kWh)", 5.27f, -1.23f
                        );


                //ENDRE DISSE!!!!
                val hoyereDagspris:Boolean = true;
                val hoyereMaanedspris:Boolean = true;
                //

                var textString = "Strømprisen nå er xx% " + if(hoyereDagspris) "over" else "under" + "dagens gjennomsnitt."
                textString += if(!hoyereDagspris){
                    "Det er ikke en dårlig ide å bruke mye strøm nå."
                } else{
                    "Det er lurt å vente med strømintense oppgaver."
                }
                Text(text = textString)

                Spacer(modifier = Modifier.size(30.dp))

                Text("Kortene på siden viser pris for aktivitet, og hvor mye dyrere eller billigere det er å gjøre aktiviteten nå i forhold til de siste 24 timene sitt gjennomsnitt")

            }
        }
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
        ContentCard(Modifier.weight(1f), aktivitet1, aktivitetPris1, aktivitetPrisDifferanse1);
        ContentCard(Modifier.weight(1f), aktivitet2, aktivitetPris2, aktivitetPrisDifferanse2);
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
