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

        //Finn knappene
        var btn = rule.onNodeWithText(PricePeriod.DAY.text);
        var btn2 = rule.onNodeWithText(PricePeriod.WEEK.text);
        var btn3 = rule.onNodeWithText(PricePeriod.MONTH.text);

        //Sjekk om de finnes
        btn.assertExists("Button does not exist");
        btn2.assertExists("Button does not exist");
        btn3.assertExists("Button does not exist");

        //Sjekk om noe skjer når man trykker på dem
        btn.assertHasClickAction();
        btn2.assertHasClickAction();
        btn3.assertHasClickAction();
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
