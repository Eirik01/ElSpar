package com.team12.ElSpar.ui

import java.time.LocalDateTime

sealed interface ElSparUiState {
    data class Success(
        val priceList: Map<LocalDateTime, Double>
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState
}