package com.team12.ElSpar.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.team12.ElSpar.model.PriceEntry
import com.team12.ElSpar.model.PricePeriod
import java.time.LocalDateTime

@Composable
fun PriceChart(
    priceList: Map<LocalDateTime, Double>,
    pricePeriod: PricePeriod,
    modifier: Modifier = Modifier,
    chartEntryModelProducer: ChartEntryModelProducer =
        priceList.entries
            .mapIndexed { index, (date, y) ->
                PriceEntry(date, index.toFloat(), y.toFloat())
            }.let { ChartEntryModelProducer(it) },
    axisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom> =
        AxisValueFormatter { value, chartValues ->
        (chartValues.chartEntryModel.entries.first().getOrNull(value.toInt()) as? PriceEntry)
            ?.localDate
            ?.run { "$hour" }
            .orEmpty()
    }
) {
    Chart(
        chart = lineChart(),
        model = chartEntryModelProducer.getModel(),
        startAxis = startAxis(),
        bottomAxis = bottomAxis(
            valueFormatter = axisValueFormatter
        ),
    )
}