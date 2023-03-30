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
import kotlinx.coroutines.Dispatchers
import com.team12.ElSpar.model.PricePeriod
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
    initialPricePeriod: PricePeriod = PricePeriod.DAY,
    initialPriceArea: PriceArea = PriceArea.NO1
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    private var currentPricePeriod = initialPricePeriod
    private var currentPriceArea = initialPriceArea

    init {
        getPowerPrice()
    }


    fun getPowerPrice(
        pricePeriod: PricePeriod = currentPricePeriod,
        priceArea: PriceArea = currentPriceArea
    ) {
        viewModelScope.launch(Dispatchers.IO){
            _uiState.value = try {
                ElSparUiState.Success(
                    currentPriceArea = priceArea,
                    currentPricePeriod = pricePeriod,
                    priceList = getPowerPriceUseCase(
                        startTime = LocalDateTime.now().minusDays(pricePeriod.days-1L),
                        priceArea = priceArea
                    )
                )
            } catch (e: IOException) {
                ElSparUiState.Error
            }
        }
    }

    fun updatePricePeriod(pricePeriod: PricePeriod) {
        currentPricePeriod = pricePeriod
        getPowerPrice()
    }

    fun updatePriceArea(priceArea: PriceArea) {
        currentPriceArea = priceArea
        getPowerPrice()
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