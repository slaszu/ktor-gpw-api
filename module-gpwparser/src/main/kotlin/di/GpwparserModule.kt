package pl.slaszu.gpwparser.di

import de.focus_shift.jollyday.core.HolidayManager
import de.focus_shift.jollyday.core.ManagerParameters
import org.koin.dsl.module
import pl.slaszu.gpwparser.api.GpwParser
import pl.slaszu.gpwparser.internal.FreeDaySpecification
import pl.slaszu.gpwparser.internal.GpwParserArchive
import pl.slaszu.gpwparser.internal.GpwParserImpl
import pl.slaszu.gpwparser.internal.GpwParserToday


fun gpwparserModule(gpwToday: String, gpwArchive: String, tmpDir: String) = module {
    single<HolidayManager> {
        HolidayManager.getInstance(ManagerParameters.create("pl"));
    }

    single<FreeDaySpecification> {
        FreeDaySpecification(holidayManager = get())
    }

    single<GpwParserToday> {
        GpwParserToday(
            url = gpwToday,
            dirTemp = tmpDir
        )
    }

    single<GpwParserArchive> {
        GpwParserArchive(
            url = gpwArchive,
        )
    }

    single<GpwParser> {
        GpwParserImpl(
            freeDaySpecification = get(),
            gpwParserToday = get(),
            gpwParserArchive = get(),
        )
    }
}