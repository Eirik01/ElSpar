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
import com.team12.ElSpar.domain.GetTemperatureDataPerLocation
import com.team12.ElSpar.model.PriceArea
import kotlinx.coroutines.Dispatchers
import com.team12.ElSpar.model.PricePeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate

class ElSparViewModel(
    private val getPowerPriceUseCase: GetPowerPriceUseCase,
    private val getTempDataPerLocation : GetTemperatureDataPerLocation,
    initialPricePeriod: PricePeriod = PricePeriod.DAY,
    initialPriceArea: PriceArea = PriceArea.NO1,
    initialEndDate: LocalDate = LocalDate.now()
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    private var currentPricePeriod = initialPricePeriod
    private var currentPriceArea = initialPriceArea
    private var currentEndDate = initialEndDate

    init {
        getPowerPrice()
        cache()
    }


    fun getPowerPrice(
        pricePeriod: PricePeriod = currentPricePeriod,
        priceArea: PriceArea = currentPriceArea,
        endDate: LocalDate = currentEndDate
    ) {
        viewModelScope.launch(Dispatchers.IO){
            _uiState.value = try {
                ElSparUiState.Success(
                    currentPriceArea = priceArea,
                    currentPricePeriod = pricePeriod,
                    currentEndDate = endDate,
                    priceList = getPowerPriceUseCase(
                        endDate = endDate,
                        period = pricePeriod,
                        area = priceArea
                    ),
                    tempList = getTempDataPerLocation(
                        // could also be a range of dates
                        station = "sn18700", //blindern,
                        // data is unavailable for the current hour and sometimes in the last hour.
                        time = LocalDateTime.now().minusHours(2).toString().substring(0,13), // ex. 2023-04-04T10
                        element = "air_temperature"
                    )
                )
            } catch (e: IOException) {
                ElSparUiState.Error
            }
        }
    }

    private fun cache(
        buffer: PricePeriod = PricePeriod.MONTH
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getPowerPriceUseCase(
                endDate = LocalDate.now(),
                period = buffer,
                area = currentPriceArea
            )
        }
    }

    fun updatePricePeriod(pricePeriod: PricePeriod) {
        currentPricePeriod = pricePeriod
        getPowerPrice()
    }

    fun updatePriceArea(priceArea: PriceArea) {
        currentPriceArea = priceArea
        getPowerPrice()
        cache()
    }

    fun dateForward() {
        currentEndDate = currentEndDate.plusDays(currentPricePeriod.days.toLong())
        getPowerPrice()
    }

    fun dateBack() {
        currentEndDate = currentEndDate.minusDays(currentPricePeriod.days.toLong())
        getPowerPrice()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as ElSparApplication
                val getTempDataPerLocation = application.container.getTemperatureDataPerLocation
                val getPowerPriceUseCase = application.container.getPowerPriceUseCase
                ElSparViewModel(
                    getPowerPriceUseCase = getPowerPriceUseCase,
                )
            }
        }
    }
}