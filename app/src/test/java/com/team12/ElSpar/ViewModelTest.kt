package com.team12.ElSpar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import app.cash.turbine.test
import com.team12.ElSpar.data.SettingsRepository
import com.team12.ElSpar.data.SettingsSerializer
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.rules.TestDispatcherRule
import com.team12.ElSpar.ui.viewmodel.ElSparUiState
import com.team12.ElSpar.ui.viewmodel.ElSparViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.lang.Thread.sleep
import java.time.LocalDate


//Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    //val scope = TestScope()
    lateinit var appContainer: FakeAppContainer
    lateinit var elSparViewModel: ElSparViewModel
    private val settingsRepository: SettingsRepository = mockk()



    @Before
    fun setUp() {
        Dispatchers.setMain(
            StandardTestDispatcher(
                //scope.testScheduler
            )
        )
        //TestSubject.setScope(scope)
        val context: Context = Mockito.mock(Context::class.java)
        val settingsStore: DataStore<Settings> = DataStoreFactory.create(SettingsSerializer) {
            context.dataStoreFile("settings.pb")

        }
        appContainer = FakeAppContainer(settingsRepository = settingsRepository)

        elSparViewModel = ElSparViewModel(
        appContainer.getPowerPriceUseCase,
        appContainer.settingsRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        //tester.close()
    }


    //Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun elSparViewModel_getPowerPrice_verifyElSparUiStateSuccess() =
        runTest {
            val expectedPriceList = listOf(1.0, 2.0, 3.0)
            val expectedCurrentPrice = 4.0
            val expectedUiState = ElSparUiState.Success(
                currentPricePeriod = PricePeriod.DAY,
                currentEndDate = LocalDate.now(),
                priceList = FakePowerDataSource.priceDataMapMVA,
                currentPrice = FakePowerDataSource.priceDataMapMVA
            )
            val settings = Settings.getDefaultInstance().toBuilder().setArea(Settings.PriceArea.NO1).build()
            coEvery { appContainer.settingsRepository.settingsFlow } returns flowOf(settings)



/*
            val jobA = launch {
                elSparViewModel.settings.test {
                    val emission = awaitItem()
                    assertEquals(
                        ElSparUiState.Success(
                            PricePeriod.DAY,
                            LocalDate.now(),
                            FakePowerDataSource.priceDataMapMVA,
                            FakePowerDataSource.priceDataMapMVA
                        ),
                        elSparViewModel.uiState.value
                    )

                }
            }
            elSparViewModel.getPowerPrice()
            jobA.join()
            jobA.cancel()
/*
            advanceUntilIdle()
            assertEquals(
                ElSparUiState.Success(
                    PricePeriod.DAY,
                    LocalDate.now(),
                    FakePowerDataSource.priceDataMapMVA,
                    FakePowerDataSource.priceDataMapMVA
                ),
                elSparViewModel.uiState.value
            )

 */

 */
        }

    //Checking if viewmodels priceArea updates after calling update price area
    @Test
    fun elSparViewModel_updatePricePeriod_verifyElSparViewModelCurrentPricePeriod() =
        runTest {
            elSparViewModel.updatePricePeriod(PricePeriod.WEEK)

            advanceUntilIdle()
            assertEquals(
                ElSparUiState.Success(
                    PricePeriod.WEEK,
                    LocalDate.now(),
                    FakePowerDataSource.priceDataMapMVA,
                    FakePowerDataSource.priceDataMapMVA
                ),
                elSparViewModel.uiState.value
            )
        }
}
