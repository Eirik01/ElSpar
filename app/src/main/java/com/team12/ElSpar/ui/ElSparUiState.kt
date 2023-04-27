package com.team12.ElSpar.ui


import com.team12.ElSpar.Settings.PriceArea
import com.team12.ElSpar.model.PricePeriod
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface ElSparUiState {
    data class Success(
        val currentPriceArea: PriceArea,
        val currentPricePeriod: PricePeriod,
        val currentEndDate: LocalDate,
        val priceList: Map<LocalDateTime, Double>,
        val currentPrice: Map<LocalDateTime, Double>
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState
    data class SelectArea(val currentPriceArea: PriceArea) : ElSparUiState

}