package com.team12.ElSpar.model

import com.patrykandpatrick.vico.core.entry.ChartEntry
import java.time.LocalDateTime

class PriceEntry(
    val localDate: LocalDateTime,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = PriceEntry(localDate, x, y)
}