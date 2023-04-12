package com.team12.ElSpar

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.ui.ElSparScreen
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class UiTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun selectButton_selectsButton(){
        rule.setContent {
            ElSparScreen(
                priceList = getPowerPricesByDate(LocalDateTime.of(1,1,1,1,1), PriceArea.NO1),
                currentPricePeriod = PricePeriod.DAY,
                onChangePricePeriod = { updatePricePeriod(it) },
                onUpdatePriceArea = {updatePriceArea(it)}
            )
        }

        //Sjekker om knappene finnes og at noe skjer når de trykkes

        //FIKS:: Sjekker hardkodet tekst. Teksten skal korrelere med knappene sin tekst, finnes i string resource
        var btn = rule.onNodeWithText(PricePeriod.DAY.text);
        var btn2 = rule.onNodeWithText(PricePeriod.WEEK.text);
        var btn3 = rule.onNodeWithText(PricePeriod.MONTH.text);


        //Trykk og sjekk om de er valgt
        btn.assertHasClickAction();
        btn.assertExists("Butten does not exist");

        btn2.assertHasClickAction();
        btn2.assertExists("Butten does not exist");

        btn3.assertHasClickAction();
        btn3.assertExists("Butten does not exist");
    }

    //Test data
     fun getPowerPricesByDate(
        date: LocalDateTime,
        area: PriceArea
    ): Map<LocalDateTime, Double>
    {
        return  mapOf<LocalDateTime, Double>(
        LocalDateTime.of(2023, 1, 30, 0, 0) to  10.0,
        LocalDateTime.of(2023, 1, 30, 1, 0) to  10.0,
        )
    }

    //Test data
    fun updatePricePeriod(pricePeriod: PricePeriod) {
    }
    fun updatePriceArea(v: PriceArea) {
    }
}
