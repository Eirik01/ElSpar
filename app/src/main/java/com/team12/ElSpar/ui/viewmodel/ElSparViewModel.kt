package com.team12.ElSpar.ui.viewmodel

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
import com.team12.ElSpar.exceptions.NoConnectionException
import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.internal.ClassValueCtorCache.cache
import kotlinx.coroutines.launch
import java.time.LocalDate

class ElSparViewModel(
    private val getPowerPriceUseCase: GetPowerPriceUseCase,
    private val settingsRepository: SettingsRepository,
    //this variable is only set as true when creating the viewModel for tests
    private val isATest: Boolean = false
) : ViewModel() {
    private val _uiState: MutableStateFlow<ElSparUiState> = MutableStateFlow(ElSparUiState.Loading)
    val uiState: StateFlow<ElSparUiState> = _uiState.asStateFlow()

    val settings: Flow<Settings> = settingsRepository.settingsFlow
    //variable only used under testing
    private var viewModelPriceArea: Settings.PriceArea = Settings.PriceArea.NO1

    private var currentPricePeriod = PricePeriod.DAY
    private var currentEndDate = LocalDate.now()

    /*dosent define Dispatcher.IO in viewModelScope
      since getPowerPriceUseCase switches to correct
      Dispatcher within its class
      */
    init {
        viewModelScope.launch() {
            if(!isATest){
                settings.collect { settings ->
                    if (!settings.initialStartupCompleted) {
                        settingsRepository.initializeValues()
                        _uiState.value = ElSparUiState.SelectArea(currentPriceArea = settings.area)
                    } else {
                        update()
                    }
                }
            }
            else{
                update()
            }

        }
    }

    private fun update() {
        getPowerPrice()
        cache()
    }

    fun getPowerPrice() {
        _uiState.value = ElSparUiState.Loading
        viewModelScope.launch() {
            if(!isATest) {
                settings.collect { settings ->
                    _uiState.value = try {
                        ElSparUiState.Success(
                            currentPricePeriod = currentPricePeriod,
                            currentEndDate = currentEndDate,
                            priceList = getPowerPriceUseCase(
                                endDate = currentEndDate,
                                period = currentPricePeriod,
                                area = settings.area
                            ),
                            currentPrice = getPowerPriceUseCase(
                                endDate = LocalDate.now(),
                                period = PricePeriod.DAY,
                                area = settings.area
                            )
                        )
                    } catch (e: NoConnectionException) {
                        ElSparUiState.Error
                    }
                }
            }else{
                _uiState.value = try {
                    ElSparUiState.Success(
                        currentPricePeriod = currentPricePeriod,
                        currentEndDate = currentEndDate,
                        priceList = getPowerPriceUseCase(
                            endDate = currentEndDate,
                            period = currentPricePeriod,
                            area = viewModelPriceArea
                        ),
                        currentPrice = getPowerPriceUseCase(
                            endDate = LocalDate.now(),
                            period = PricePeriod.DAY,
                            area = viewModelPriceArea
                        )
                    )
                } catch (e: NoConnectionException) {
                    ElSparUiState.Error
                }

            }
        }
    }

    private fun cache(
        buffer: PricePeriod = PricePeriod.MONTH
    ) {
        viewModelScope.launch() {
            if(!isATest){
                settings.collect { settings ->
                    getPowerPriceUseCase(
                        endDate = LocalDate.now(),
                        period = buffer,
                        area = settings.area
                    )
                }
            }
            else{
                getPowerPriceUseCase(
                    endDate = LocalDate.now(),
                    period = buffer,
                    area = viewModelPriceArea
                )
            }
        }
    }

    fun updatePricePeriod(pricePeriod: PricePeriod) {
        currentPricePeriod = pricePeriod
        update()
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
        update()
    }

    fun dateBack() {
        currentEndDate = currentEndDate.minusDays(currentPricePeriod.days.toLong())
        update()
    }

    fun updatePreference(priceArea: Settings.PriceArea) {
        viewModelScope.launch() {
            if(!isATest){
                settingsRepository.updatePriceArea(priceArea)
                settingsRepository.initialStartupCompleted()
            }else{
                viewModelPriceArea = priceArea
            }
            update()
        }
    }

    fun updatePreference(activity: Settings.Activity, value: Int) {
        viewModelScope.launch() {
            settingsRepository.updateActivity(activity, value)
        }
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