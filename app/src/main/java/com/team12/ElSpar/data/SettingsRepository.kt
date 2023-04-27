package com.team12.ElSpar.data

import androidx.datastore.core.DataStore
import com.team12.ElSpar.Settings
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

private const val SHOWER_TIME = 10

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    suspend fun initialStartupCompleted()
    suspend fun updatePriceArea(area: Settings.PriceArea)
    suspend fun initializeValues()
}

class DefaultSettingsRepository(
    private val settingsStore: DataStore<Settings>
) : SettingsRepository {
    override val settingsFlow: Flow<Settings> = settingsStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(Settings.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun initialStartupCompleted() {
        settingsStore.updateData { settings ->
            settings.toBuilder().setInitialStartupCompleted(true).build()
        }
    }

    override suspend fun updatePriceArea(area: Settings.PriceArea) {
        settingsStore.updateData { settings ->
            settings.toBuilder().setArea(area).build()
        }
    }

    override suspend fun initializeValues() {
        settingsStore.updateData { settings ->
            settings.toBuilder().setShowerTime(SHOWER_TIME).build()
        }
    }
}