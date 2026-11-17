package pl.slaszu.gpwparser.internal

import kotlinx.datetime.LocalDate
import org.apache.commons.io.FileUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import pl.slaszu.gpwparser.api.StockPriceDTO
import java.io.File
import java.net.URI


internal class GpwParserToday(
    private val url: String, private val dirTemp: String
) {

    fun getDataForDate(date: LocalDate): List<StockPriceDTO> {

        val file = File(this.dirTemp, "today_latest.xls")

        FileUtils.copyURLToFile(URI(this.url).toURL(), file, 3000, 3000)

        val document: Document = Jsoup.parse(file)

        val table_tr: Elements = document.select("table tr")

        // missing two rows
        table_tr.removeAt(0)
        table_tr.removeAt(0)

        val stockCodes: MutableList<StockPriceDTO> = ArrayList()

        for (row in table_tr) {
            // find td elements in row
            val cells: Elements = row.select("td")

            if (cells.get(2).text().isEmpty()) {
                break
            }

            stockCodes.add(
                this.fromTableCells(cells, date)
            )
        }

        return stockCodes.toList()
    }

    private fun fromTableCells(cells: Elements, date: LocalDate): StockPriceDTO {
        return StockPriceDTO(
            cells.get(4).text(),  // code
            cells.get(2).text(),  // name
            this.fromStringToFloat(cells.get(9).text()),  // open
            this.fromStringToFloat(cells.get(11).text()),  // high
            this.fromStringToFloat(cells.get(10).text()),  // low
            this.fromStringToFloat(cells.get(12).text()),  // price
            this.fromStringToFloat(cells.get(23).text()).toInt(),  // volumen
            (this.fromStringToFloat(cells.get(24).text()) * 1000).toInt(),  // amount
            date
        )
    }

    private fun fromStringToFloat(string: String): Float {
        val stringX = string.replace(",", ".").replace("[^0-9.]".toRegex(), "")
        if (stringX.isEmpty()) {
            return 0f
        }

        return stringX.toFloat()
    }
}
