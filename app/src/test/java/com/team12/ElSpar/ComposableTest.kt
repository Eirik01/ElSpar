package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.rules.TestDispatcherRule
import com.team12.ElSpar.ui.ElSparUiState
import com.team12.ElSpar.ui.ElSparViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class ComposableTest {
    /*
    @get:Rule
    val rule = createComposeRule()*/


}
