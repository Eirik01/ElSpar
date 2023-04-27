package com.team12.ElSpar.ui.viewmodel


import com.team12.ElSpar.Settings.PriceArea
import com.team12.ElSpar.model.PricePeriod
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface MainUiState {
    data class Success(
        val currentPriceArea: PriceArea,
        val currentPricePeriod: PricePeriod,
        val currentEndDate: LocalDate,
        val priceList: Map<LocalDateTime, Double>
    ) : MainUiState
    object Loading : MainUiState
    object Error : MainUiState
    data class SelectArea(val currentPriceArea: PriceArea) : MainUiState

}