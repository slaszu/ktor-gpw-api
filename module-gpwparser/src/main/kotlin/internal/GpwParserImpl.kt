package pl.slaszu.gpwparser.internal

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.todayIn
import pl.slaszu.gpwparser.api.FreeDayException
import pl.slaszu.gpwparser.api.GpwParser
import pl.slaszu.gpwparser.api.StockPriceDTO
import kotlin.time.Clock

internal class GpwParserImpl(
    private val freeDaySpecification: FreeDaySpecification,
    private val gpwParserToday: GpwParserToday,
    private val gpwParserArchive: GpwParserArchive
) : GpwParser {
    override fun getDataForDate(date: LocalDate): List<StockPriceDTO> {
        if (freeDaySpecification.isSatisfiedBy(date)) {
            throw FreeDayException.fromDate(date)
        }

        val now = Clock.System.todayIn(TimeZone.currentSystemDefault())
        if (now < date) {
            throw IllegalArgumentException("Data for date $date is not satisfied by now")
        }

        if (date == now) {
            return gpwParserToday.getDataForDate(date)
        }
        return gpwParserArchive.getDataForDate(date)
    }
}