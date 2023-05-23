package com.team12.ElSpar.fake

import com.team12.ElSpar.Settings
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow

enum class PriceArea {
    NO1,
    NO2,
    NO3,
    NO4,
    NO5,
}

object FakeSettings {
    val initialStartupCompleted = 1
    var area = PriceArea.NO1;

    enum class Activity {
        SHOWER,
        WASH,
        OVEN,
        CAR
    }
    /*
    int32 shower = 3;
    int32 wash = 4;
    int32 oven = 5;
    int32 car = 6;

     */
}