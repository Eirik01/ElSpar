package com.team12.ElSpar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.application.Settings
import com.team12.ElSpar.ElSparApplication
import com.team12.ElSpar.data.SettingsRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
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
    private val settingsRepository: SettingsRepository,
    initialPricePeriod: PricePeriod = PricePeriod.DAY,
    initialEndDate: LocalDate = LocalDate.now()
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> =
        MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    private var currentPricePeriod = initialPricePeriod
    private var currentEndDate = initialEndDate
    private lateinit var currentPriceArea: Settings.PriceArea

    init {
        startup()
    }

    private fun startup() {
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

    fun updatePriceArea(priceArea: Settings.PriceArea) {
        viewModelScope.launch(Dispatchers.IO) {
            currentPriceArea = priceArea
            settingsRepository.updatePriceArea(priceArea)
            settingsRepository.initialStartupCompleted()
            getPowerPrice()
            cache()
        }
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