package pl.slaszu.core.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import pl.slaszu.core.api.StockPriceRepository
import pl.slaszu.core.api.StockRepository
import pl.slaszu.core.internal.repository.ExposedStockPriceRepository
import pl.slaszu.core.internal.repository.ExposedStockRepository

fun coreModule(url: String, user: String, pass: String) = module {
    single<Database> {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"
            jdbcUrl = url
            username = user
            password = pass
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }

    single<StockRepository> {
        ExposedStockRepository(db = get())
    }

    single<StockPriceRepository> {
        ExposedStockPriceRepository(db = get())
    }
}