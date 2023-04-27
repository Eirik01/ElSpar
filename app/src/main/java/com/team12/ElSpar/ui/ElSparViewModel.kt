package com.team12.ElSpar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team12.ElSpar.Settings
import com.team12.ElSpar.ElSparApplication
import com.team12.ElSpar.data.SettingsRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import kotlinx.coroutines.Dispatchers
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.network.NoConnectionException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ElSparViewModel(
    private val getPowerPriceUseCase: GetPowerPriceUseCase,
    private val settingsRepository: SettingsRepository,
    initialPricePeriod: PricePeriod = PricePeriod.DAY,
    initialEndDate: LocalDate = LocalDate.now(),
    //initialCoordinates : Pair<String,String> = "60.79" to "11.08" // parametres in locationforecast2.0 API
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    private var currentPricePeriod = initialPricePeriod
    private var currentEndDate = initialEndDate
    private lateinit var currentPriceArea: Settings.PriceArea
    //private var currentCoordinates = initialCoordinates

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.settingsFlow.collect { settings ->
                if (!settings.initialStartupCompleted) {
                    _uiState.value = ElSparUiState.SelectArea(
                        currentPriceArea = settings.area
                    )
                } else {
                    currentPriceArea = settings.area
                    getPowerPrice()
                    cache()
                }
            }
        }
    }

    fun getPowerPrice(
        pricePeriod: PricePeriod = currentPricePeriod,
        priceArea: Settings.PriceArea = currentPriceArea,
        endDate: LocalDate = currentEndDate
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = ElSparUiState.Success(
                    currentPriceArea = priceArea ,
                    currentPricePeriod = pricePeriod,
                    currentEndDate = endDate,
                    priceList = getPowerPriceUseCase(
                        endDate = endDate,
                        period = pricePeriod,
                        area = priceArea
                    ),
                    currentPrice = getPowerPriceUseCase(
                        endDate = LocalDate.now(),
                        period = PricePeriod.DAY,
                        area = priceArea,
                    )
                )
            } catch (e: NoConnectionException) {
                _uiState.value = ElSparUiState.Error
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

    fun updatePriceArea(priceArea: Settings.PriceArea) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPriceArea = priceArea
            settingsRepository.updatePriceArea(priceArea)
            settingsRepository.initialStartupCompleted()
            getPowerPrice()
            cache()
        }
    }

    /*fun updateCoordinatesForPriceArea(priceArea: PriceArea){
        currentCoordinates = when (priceArea){
            PriceArea.NO1 -> "60.79" to "11.08"
            PriceArea.NO2 -> "59.14" to "7.80"
            PriceArea.NO3 -> "63.03" to "9.65"
            PriceArea.NO4 -> "68.29" to "17.53"
            else -> "60.83" to "7.61"
        }
        //Log.d("priceCoords",currentCoordinates.toString())
    }*/

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
                (this[APPLICATION_KEY] as ElSparApplication).container.run {
                    ElSparViewModel(
                        getPowerPriceUseCase = getPowerPriceUseCase,
                        settingsRepository = settingsRepository
                    )
                }
            }
        }
    }
}