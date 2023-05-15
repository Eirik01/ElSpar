package com.team12.ElSpar.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team12.ElSpar.R

@Composable
fun InfoScreen(
    modifier : Modifier = Modifier
) {
    val imageList: List<Int> = listOf(
        R.drawable.powerpic,
        R.drawable.showerpic,
        R.drawable.prognosepic,
        R.drawable.paymentpic
    )
    val textList: List<Triple<String, String, String>> = listOf(
        Triple(
            "Hva er en 'kWh'?",
            "Mange ser kWh bli skrevet men veit ikke helt hva det er",
            "En kilowattime er det samme som 1000 wh, eller 1000 watt-timer. Om du har ett apparat som bruker en watt, vil denne på en time bruke en wh, og et apparat som bruker 1000 watt vil på en time bruke en kWh. Et kjøleskap for eksempel bruker vanlig vis rundt 200 watt, og må stå på i 5 timer for å bruke en kwh."
        ),
        Triple(
            "Aktiviteter",
            "Hvilke antagelser er gjort?",
            "I strøm-kalkulatoren har vi vært nødt til å gjøre noen antagelser for å gjøre den funksjonell. Antagelser har derfor blitt gjort for de forskjellige aktiviteterna. Vi har antatt at: En dusj bruker rundt 5kWh, en klesvask bruker runt 0.4kWh og at en ovn bruker runt 3.5kWh. Hvor stor kapasitet el-bilen din har, fører du selv inn."
        ),
        Triple("Prognose", "Hva baserer prognosen seg på?", "SEBBERS"),
        Triple(
            "Spotpris",
            "Hva bestemmer spotprisene?",
            "I nord-europa har vi en kraftbørs som heter Nord Pool. Der møtes strømprodusenter og leverandører for å bestemme en strømpris. Strømprisene blir for det meste basert på etterspørsel og tilbud, altså høy etterspørsel og lavt tilbud gir dyr strøm. Man kan se svingninger i etterspørsel både i året (mye om vinter lite om sommer), og i dagen (lite midt på natten, mye på ettermiddagen). Derfor vil strømprisen du betaler variere med dette, altså høye priser om vinter og ettermiddagen. Produksjonen varierer også, når Norge og de andre landene i nord pool produserer mye strøm vil prisen være lav, og motsatt. I tillegg påvirkes den av andre faktorer som kurs og forskjellige avgifter. Prisen for en gitt dag bestemmes dagen før, rundt klokken 13-14. "
        )
    )
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = CenterHorizontally

    ) {
        items(textList.size) {

            Column {
                Box {
                    Image(
                        painter = painterResource(id = imageList[it]),
                        contentDescription = "My Image",
                        alpha = 0.3f,
                        modifier = Modifier
                            .blur(
                                radiusX = 10.dp,
                                radiusY = 10.dp,
                                edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(8.dp))
                            )
                        //modifier = modifier.offset(y = (200).dp).clip(RoundedCornerShape(15.dp))
                    )
                    Column(modifier = Modifier
                        .padding(60.dp)
                        .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = CenterHorizontally

                    ) {
                        Text(
                            text = textList[it].first,
                            modifier = Modifier
                                .padding(top = 10.dp)
                                .fillMaxWidth(0.8f),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = textList[it].second,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxWidth(0.8f),
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(bottom = 30.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                ) {

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
}
