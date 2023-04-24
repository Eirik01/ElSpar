package com.team12.ElSpar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Observation(
    @SerialName("air_temperature")
    val air_temperature: Double,
    @SerialName("wind_speed")
    val wind_speed: Double,
)

data class ObservationData(
    val timeseries: List<Observation>
)