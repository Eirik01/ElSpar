package com.team12.ElSpar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Observation(
    @SerialName("elementId")
    val elementId: String,
    @SerialName("value")
    val value: Double,
    @SerialName("unit")
    val unit: String,
    @SerialName("timeOffset")
    val timeOffset: String,
    @SerialName("timeResolution")
    val timeResolution: String
)

@Serializable
data class ObservationData(
    @SerialName("sourceId")
    val sourceId: String,
    @SerialName("referenceTime")
    val referenceTime: String,
    @SerialName("observations")
    val observations: List<Observation>
)