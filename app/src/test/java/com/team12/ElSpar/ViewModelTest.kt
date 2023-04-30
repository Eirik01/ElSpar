package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.domain.GetTemperatureUseCase
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.rules.MainCoroutineRule
import com.team12.ElSpar.rules.TestDispatcherRule
import com.team12.ElSpar.ui.ElSparUiState
import com.team12.ElSpar.ui.ElSparViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import java.time.LocalDate


//Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    //Creating a dispatcher which can be used for all test within this class
    //@get:Rule
    //val testDispatcher1 = TestDispatcherRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    val appContainer = FakeAppContainer()

    private var elSparViewModel: ElSparViewModel = ElSparViewModel(
        appContainer.getPowerPriceUseCase,
        appContainer.getTemperatureUseCase,
    )

    /*
    @Before
    fun setUp(){
            elSparViewModel = ElSparViewModel(
                appContainer.getPowerPriceUseCase,
                appContainer.getTemperatureUseCase,
            )
    }

     */





            //Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun elSparViewModel_getPowerPrice_verifyElSparUiStateSuccess() =
        runTest{
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)

            Dispatchers.setMain(testDispatcher)
            elSparViewModel.getPowerPrice()
            advanceUntilIdle()
            withTimeout(10000){
                while(elSparViewModel.uiState.value is ElSparUiState.Loading ){
                    sleep(500)
                }
            }

            assertEquals(
                ElSparUiState.Success(PriceArea.NO1, PricePeriod.DAY, LocalDate.now(), FakePowerDataSource.priceDataMapMVA),
                elSparViewModel.uiState.value
            )
        }

    @Test
    fun elSparViewModel_updatePricePeriod_verifyUiState(){
        runTest {
            elSparViewModel.updatePricePeriod(PricePeriod.WEEK)
            advanceUntilIdle()
            //sleep(1000)
            //elSparViewModel.uiState.value.await()
        }
            assertEquals(
                ElSparUiState.Success(PriceArea.NO1, PricePeriod.WEEK, LocalDate.now(), FakePowerDataSource.priceDataMapMVA),
                elSparViewModel.uiState.value
            )

        }

    //Checking if viewmodels priceArea updates after calling update price area
    @Test
    fun elSparViewModel_updatePriceArea_verifyElSparUiStateCurrentPriceArea() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            val testDispatcher = UnconfinedTestDispatcher(testScheduler)
            Dispatchers.setMain(testDispatcher)

            try {
                elSparViewModel.getPowerPrice()
                elSparViewModel.updatePriceArea(PriceArea.NO2)
                //sleep(2000)// Uses testDispatcher, runs its coroutine eagerly
                assertEquals(
                    ElSparUiState.Success(
                        PriceArea.NO2,
                        PricePeriod.DAY,
                        LocalDate.now(),
                        FakePowerDataSource.priceDataMapMVA
                    ),
                    elSparViewModel.uiState.value
                )
            } finally {
                Dispatchers.resetMain()
            }
        }
}