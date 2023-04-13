package com.team12.ElSpar.ui

import com.team12.ElSpar.model.Observation
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import java.time.Duration
import java.time.LocalDateTime

sealed interface ElSparUiState {
    data class Success(
        val currentPriceArea: PriceArea,
        val currentPricePeriod: PricePeriod,
        val priceList: Map<LocalDateTime, Double>,
        val tempList : List<Double>
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState

}