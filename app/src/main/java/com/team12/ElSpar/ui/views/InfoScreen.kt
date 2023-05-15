package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoScreen(
    modifier : Modifier = Modifier
) {
        val textList : List<Triple<String,String,String>> = listOf(
            Triple("Hva er en 'kWh'?" , "Mange ser kWh bli skrevet men veit ikke helt hva det er", "En kilowattime er det samme som 1000 wh, eller 1000 watt-timer. Om du har ett apparat som bruker en watt, vil denne på en time bruke en wh, og et apparat som bruker 1000 watt vil på en time bruke en kWh. Et kjøleskap for eksempel bruker vanlig vis rundt 200 watt, og må stå på i 5 timer for å bruke en kwh."),
            Triple("Aktiviteter" , "Hvilke antagelser er gjort?", "I strøm-kalkulatoren har vi vært nødt til å gjøre noen antagelser for o gjøre den brukervennlig. Vi regner blant annet med at XXXXX"),
            Triple("Prognose" , "Hva baserer prognosen seg på?", "Denne tar sebbe eller??"),
            Triple("Spotpris" , "Hva bestemmer spotprisene?", "I nord-europa har vi en kraftbørs som heter Nord Pool. Der møtes strømprodusenter og leverandører for å bestemme en strømpris. Strømprisene blir for det meste basert på etterspørsel og tilbud, altså høy etterspørsel og lavt tilbud gir dyr strøm. Man kan se svingninger i etterspørsel både i året (mye om vinter lite om sommer), og i dagen (lite midt på natten, mye på ettermiddagen). Derfor vil strømprisen du betaler variere med dette, altså høye priser om vinter og ettermiddagen. Produksjonen varierer også, når Norge og de andre landene i nord pool produserer mye strøm vil prisen være lav, og motsatt. I tillegg påvirkes den av andre faktorer som kurs og forskjellige avgifter. Prisen for en gitt dag bestemmes dagen før, rundt klokken 13-14. "),

            )
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){items(textList.size){
            Text(
                text = textList[it].first,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(top = 10.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
                )
            Text(
                text = textList[it].second,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .fillMaxWidth(0.8f),
                fontSize = 16.sp

            )
            Card(
                modifier = Modifier
                    .padding( bottom = 30.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
            ){

                Text(
                    text = textList[it].third,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(10.dp),

                    )
            }
        }
    }
}
