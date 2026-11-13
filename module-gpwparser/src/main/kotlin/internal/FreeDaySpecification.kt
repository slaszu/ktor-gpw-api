package pl.slaszu.gpwparser.internal

import de.focus_shift.jollyday.core.HolidayManager
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

internal class FreeDaySpecification(
    private val holidayManager: HolidayManager,
) {
    fun isSatisfiedBy(date: LocalDate): Boolean {
        return isWeekend(date) || isHoliday(date)
    }

    private fun isHoliday(date: LocalDate): Boolean {
        return holidayManager.isHoliday(date.toJavaLocalDate(),"pl")
    }

    private fun isWeekend(date: LocalDate): Boolean {
        return date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
    }
}