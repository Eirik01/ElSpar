package com.team12.ElSpar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AboutUsScreen(){

    Column(

    ){
        Text(
            text = "ElSpar har blivit utvecklat av en grupp på 6 informatikstudenter på UIO " +
                    "inom kurset IN2000 - Software Engineering med prosjektarbeid.\n" +
                    "Vårt mål med denna appen- bortsätt från att det är väldigt relevant träning i " +
                    "apputveckling, var att upplysa folk på strömpriser. Dessutom tror vi " +
                    "starkt på att denna appen kan få brukarna " +
                    "att spara en del pengar, bara utav att ändra på deras redan existerande rutiner. \n" +
                    "Anledningen varför vi tycker att strømpriser ær viktiga ær før att vi sjælva sliter " +
                    "ekonomiskt med att betala strømrækningen, vidare tycker vi att det ær viktigt att kæmpa" +
                    "mot glashuseffekten. Vi tyckte att det inte fanns ett mer relevant sætt att gøra detta på " +
                    "æn med en strøm-app"
        )
    }
}