package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AboutUsScreen(){

    Column(

    ){
        Card(
            modifier = Modifier.padding(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        ){
            Text(
                text = "ElSpar har blitt utviklet av en gruppe på 6 informatikkstudenter fra UIO " +
                        "i kurset IN2000 - Software Engineering med prosjektarbeid.\n\n" +
                        "Målet vårt med denne appen, bortsett fra at vi må lage en app for " +
                        "å få en god karakter, var å opplyse folk  om strømpriser. Dessuten tror vi " +
                        "at denne appen kan få brukerene " +
                        "til å spare en del penger ved å endre rutinene deres. \n\n" +
                        "Vi mener at strømpriser er viktige ettersom vi selv sliter " +
                        "med å betale strømrekningen, og har snakket med flere som sliter med det samme problemet. " +
                        "Vi kom fram til at den beste måten vi kan ha innvirkning på dette er ved å utvikle en app "+
                        "som hjelper folk med å redusere sine strømutgifter ved å endre sine vaner.",
                modifier = Modifier.
                padding(10.dp),

            )
        }

    }
}