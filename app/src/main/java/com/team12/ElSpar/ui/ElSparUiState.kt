package com.team12.ElSpar.ui

sealed interface ElSparUiState {
    data class Success(
        val currentPowerPrice: Double
    ) : ElSparUiState
    object Loading : ElSparUiState
    object Error : ElSparUiState
}