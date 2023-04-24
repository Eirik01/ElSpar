package com.team12.ElSpar.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team12.ElSpar.ElSparApplication
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetTemperatureUseCase
import com.team12.ElSpar.model.PriceArea
import kotlinx.coroutines.Dispatchers
import com.team12.ElSpar.model.PricePeriod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.LocalDate

class ElSparViewModel(
    private val getPowerPriceUseCase: GetPowerPriceUseCase,
    private val getTemperatureUseCase: GetTemperatureUseCase, // can be removed if weather data is not used in UI
    initialPricePeriod: PricePeriod = PricePeriod.DAY,
    initialPriceArea: PriceArea = PriceArea.NO1,
    initialEndDate: LocalDate = LocalDate.now(),
    initialCoordinates : Pair<String,String> = "60.79" to "11.08" // parametres in locationforecast2.0 API
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    private var currentPricePeriod = initialPricePeriod
    private var currentPriceArea = initialPriceArea
    private var currentEndDate = initialEndDate
    private var currentCoordinates = initialCoordinates

    init {
        getPowerPrice()
        cache()
    }


    fun getPowerPrice(
        pricePeriod: PricePeriod = currentPricePeriod,
        priceArea: PriceArea = currentPriceArea,
        endDate: LocalDate = currentEndDate,
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

    fun updateCoordinatesForPriceArea(priceArea: PriceArea){
        currentCoordinates = when (priceArea){
            PriceArea.NO1 -> "60.79" to "11.08"
            PriceArea.NO2 -> "59.14" to "7.80"
            PriceArea.NO3 -> "63.03" to "9.65"
            PriceArea.NO4 -> "68.29" to "17.53"
            else -> "60.83" to "7.61"
        }
        //Log.d("priceCoords",currentCoordinates.toString())
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
                val getPowerPriceUseCase = application.container.getPowerPriceUseCase
                val getTemperatureUseCase = application.container.getTemperatureUseCase

                ElSparViewModel(
                    getPowerPriceUseCase = getPowerPriceUseCase,
                    getTemperatureUseCase = getTemperatureUseCase
                )
            }
        }
    }
}