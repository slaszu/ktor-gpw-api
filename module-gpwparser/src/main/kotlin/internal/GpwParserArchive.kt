package pl.slaszu.gpwparser.internal

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import pl.slaszu.gpwparser.api.StockPriceDTO
import java.net.URL
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date


internal class GpwParserArchive(
    private val url: String,
) {

    fun getDataForDate(date: LocalDate): List<StockPriceDTO> {
        // prepare url
        val utilDate = Date.from(
            date
                .toJavaLocalDate()
                .atStartOfDay(ZoneId.systemDefault()) // lub ZoneOffset.UTC jeśli wolisz UTC
                .toInstant()
        )
        val format = SimpleDateFormat("dd-MM-yyyy")
        val currentUrl = this.url.replace("[date]", format.format(utilDate))


        val workbook: Workbook = HSSFWorkbook(URL(currentUrl).openStream())
        val sheet: Sheet = workbook.getSheetAt(0)
        val iterator: MutableIterator<Row> = sheet.rowIterator()

        val result: MutableList<StockPriceDTO> = ArrayList()
        // skip first row
        iterator.next()
        while (iterator.hasNext()) {
            val row: Row = iterator.next()

            result.add(this.fromRow(row))
        }

        return result.toList()
    }

    private fun fromRow(row: Row): StockPriceDTO {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        val date: Date = formatter.parse(row.getCell(0).getStringCellValue())

        return StockPriceDTO(
            "",  // code, not exists in gpw.pl
            row.getCell(1).getStringCellValue(),  // name
            row.getCell(4).getNumericCellValue().toFloat(),  // open
            row.getCell(5).getNumericCellValue().toFloat(),  // high
            row.getCell(6).getNumericCellValue().toFloat(),  // low
            row.getCell(7).getNumericCellValue().toFloat(),  // price
            row.getCell(9).getNumericCellValue().toInt(),  // volumen
            (row.getCell(11).getNumericCellValue() * 1000).toInt(),  // amount
            date.toInstant()
                .atZone(ZoneId.systemDefault()) // lub np. ZoneOffset.UTC
                .toLocalDate()
                .toKotlinLocalDate()
        )
    }
}