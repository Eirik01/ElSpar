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
                text = "ElSpar har blitt utviklet av en gruppe med 6 informatikkstudenter på UIO " +
                        "inom kurset IN2000 - Software Engineering med prosjektarbeid.\n" +
                        "Vårt mål med denna appen- bortsätt från att det är väldigt relevant träning i " +
                        "apputveckling, var att upplysa folk på strömpriser. Dessutom tror vi " +
                        "starkt på att denna appen kan få brukarna " +
                        "att spara en del pengar, bara utav att ändra på deras redan existerande rutiner. \n" +
                        "Anledningen varför vi tycker att strømpriser ær viktiga ær før att vi sjælva sliter " +
                        "ekonomiskt med att betala strømrækningen, vidare tycker vi att det ær viktigt att kæmpa" +
                        "mot glashuseffekten. Vi tyckte att det inte fanns ett mer relevant sætt att gøra detta på " +
                        "æn med en strøm-app",
                modifier = Modifier.
                padding(10.dp),

            )
        }

    }
}