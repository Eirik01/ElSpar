package com.team12.ElSpar.ui

import java.time.Duration
import java.time.LocalDateTime

sealed interface ElSparUiState {
    data class Success(
        val priceList: Map<LocalDateTime, Double>,
        var startTime: LocalDateTime,
        var endTime: LocalDateTime
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState

}