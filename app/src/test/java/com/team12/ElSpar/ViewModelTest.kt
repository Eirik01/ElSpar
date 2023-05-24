package com.team12.ElSpar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.team12.ElSpar.data.DefaultSettingsRepository
import com.team12.ElSpar.data.SettingsRepository
import com.team12.ElSpar.data.SettingsSerializer
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.ui.viewmodel.ElSparUiState
import com.team12.ElSpar.ui.viewmodel.ElSparViewModel
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.lang.Thread.sleep
import java.time.LocalDate
import kotlinx.coroutines.flow.FlowCollector


//Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    //val scope = TestScope()
    lateinit var appContainer: FakeAppContainer
    lateinit var elSparViewModel: ElSparViewModel
    @Before
    fun setUp() {
        Dispatchers.setMain(
            StandardTestDispatcher()
        )
        val settingsStore: DataStore<Settings> = DataStoreFactory.create(SettingsSerializer) {
            File("fake_test_file")
        }
        val settingsRepository = DefaultSettingsRepository(settingsStore)
        appContainer = FakeAppContainer(settingsRepository = settingsRepository)

        elSparViewModel = ElSparViewModel(
            appContainer.getPowerPriceUseCase,
            appContainer.settingsRepository,
            isATest = true
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }



    //Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun elSparViewModel_getPowerPrice_verifyElSparUiStateSuccess() =
        runTest{

            elSparViewModel.getPowerPrice()
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

    @Test
    fun elSparViewModel_updatePreference_priceArea_verifyChange() =
        runTest {
            elSparViewModel.updatePreference(Settings.PriceArea.NO2)
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
    }

    @Test
    fun elSparViewModel_dateBack_verifyDate() =
        runTest{
            elSparViewModel.dateBack()
            advanceUntilIdle()
            assertEquals(
                ElSparUiState.Success(
                    PricePeriod.DAY,
                    LocalDate.now().minusDays(1),
                    FakePowerDataSource.priceDataMapMVA,
                    FakePowerDataSource.priceDataMapMVA
                ),
                elSparViewModel.uiState.value
            )
        }
}


        //TestSubject.setScope(scope)
/*
        val context: Context = mockk.mock(Context::class.java)
        val settingsStore: DataStore<Settings> = DataStoreFactory.create(SettingsSerializer) {
            context.dataStoreFile("settings.pb")

        }
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

            val settings = Settings.getDefaultInstance().toBuilder().setArea(Settings.PriceArea.NO1).build()
            coEvery { appContainer.settingsRepository.settingsFlow } returns flowOf(settings)


    @Test
    fun `getPowerPrice emits success state when settings flow emits a value`() = runBlockingTest {
        val mockSettingsFlow = mockk<Flow<Settings>>()
        val testSettingsRepository = object : SettingsRepository {
            override val settingsFlow: Flow<Settings> = mockSettingsFlow
            override val iODispatcher: CoroutineDispatcher = Dispatchers.Unconfined // Replace with a test dispatcher if needed
        }
        val mockUseCase = mockk<GetPowerPriceUseCase>()
        every { mockUseCase(any(), any(), any()) } returns listOf(1.0, 2.0, 3.0)



        uiState.assertValues(
            ElSparUiState.Loading,
            ElSparUiState.Success(
                currentPricePeriod = ..., // Replace with expected values
                currentEndDate = ...,
            priceList = listOf(1.0, 2.0, 3.0),
            currentPrice = listOf(1.0, 2.0, 3.0)
            )
        )
    }

 */
