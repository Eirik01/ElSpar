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
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.exceptions.NoConnectionException
import kotlinx.coroutines.flow.*
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

    init {
        viewModelScope.launch {
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
        viewModelScope.launch {
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
        viewModelScope.launch {
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

    fun dateForward() {
        currentEndDate = currentEndDate.plusDays(currentPricePeriod.days.toLong())
        update()
    }

    fun dateBack() {
        currentEndDate = currentEndDate.minusDays(currentPricePeriod.days.toLong())
        update()
    }

    fun updatePreference(priceArea: Settings.PriceArea) {
        viewModelScope.launch {
            if(!isATest){
                currentEndDate = LocalDate.now()
                settingsRepository.updatePriceArea(priceArea)
                settingsRepository.initialStartupCompleted()
            }else{
                viewModelPriceArea = priceArea
            }
            update()
        }
    }

    fun updatePreference(activity: Settings.Activity, value: Int) {
        viewModelScope.launch {
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