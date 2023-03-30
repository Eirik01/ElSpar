package com.team12.ElSpar.ui

import com.team12.ElSpar.model.PricePeriod
import java.time.Duration
import java.time.LocalDateTime

sealed interface ElSparUiState {
    data class Success(
        val priceList: Map<LocalDateTime, Double>
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState

}