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
            Triple("Hva er en 'kWh'?" , "Mange ser kWh bli skrevet men veit ikke helt hva det er", "f"),
            Triple("Aktiviteter" , "Hvilke antagelser er gjort?", "f"),
            Triple("Strømpriser" , "Hva er greien med strømpriser?", "c"),
            Triple("Prognos" , "Hva baserer prognosen seg på?", "i"),
            Triple("Spotpris" , "Hva bestemmer spotprisene?", "i"),

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
