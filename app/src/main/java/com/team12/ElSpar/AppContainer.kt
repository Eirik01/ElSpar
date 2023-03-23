package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase

interface AppContainer {
    val getPowerPriceUseCase: GetPowerPriceUseCase
    val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase
}

class DefaultAppContainer : AppContainer {
    private val powerRepository = DefaultPowerRepository()
    private val weatherRepository = DefaultWeatherRepository()

    override val getPowerPriceUseCase: GetPowerPriceUseCase =
        GetPowerPriceUseCase(
            powerRepository = powerRepository
        )

    override val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase =
        GetProjectedPowerPriceUseCase(
            powerRepository = powerRepository,
            weatherRepository = weatherRepository
        )
}