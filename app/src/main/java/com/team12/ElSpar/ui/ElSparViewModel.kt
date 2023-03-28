package com.team12.ElSpar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team12.ElSpar.ElSparApplication
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.model.PriceArea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDateTime

class ElSparViewModel(
    private val getPowerPriceUseCase: GetPowerPriceUseCase,
    private val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    init {
        getPowerPrice()
    }

    fun getPowerPrice(
        starttime: LocalDateTime = LocalDateTime.now(),
        endtime: LocalDateTime = LocalDateTime.now(),

        ) {
        viewModelScope.launch {
            _uiState.value = try {

                ElSparUiState.Success(
                    priceList = getPowerPriceUseCase(),
                    startTime = starttime,
                    endTime = endtime,

                    )
            } catch (e: IOException) {
                ElSparUiState.Error
            }
        }
    }

    fun updateInterval(
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ){
        viewModelScope.launch {
            _uiState.update { currentState ->
                (currentState as ElSparUiState.Success).copy(
                    priceList = getPowerPriceUseCase(
                        startTime = startTime,
                        endTime = endTime
                    )
                )
            }
        }
    }

    fun updatePriceArea(
        priceArea: PriceArea
    ){
        viewModelScope.launch {
            _uiState.update { currentState ->
                (currentState as ElSparUiState.Success).copy(
                    priceList = getPowerPriceUseCase(priceArea = priceArea)
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY] as ElSparApplication)
                val getCurrentPowerPriceUseCase = application.container.getPowerPriceUseCase
                val getProjectedPowerPriceUseCase = application.container.getProjectedPowerPriceUseCase
                ElSparViewModel(
                    getPowerPriceUseCase = getCurrentPowerPriceUseCase,
                    getProjectedPowerPriceUseCase = getProjectedPowerPriceUseCase,
                )
            }
        }
    }
}