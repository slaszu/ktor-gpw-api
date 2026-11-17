package pl.slaszu.database.di

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.dsl.module
import pl.slaszu.database.api.StockPricePersister
import pl.slaszu.database.api.StockPriceRepository
import pl.slaszu.database.api.StockRepository
import pl.slaszu.database.internal.persister.StockPricePersisterImpl
import pl.slaszu.database.internal.repository.ExposedStockPriceRepository
import pl.slaszu.database.internal.repository.ExposedStockRepository

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

    single<StockPricePersister> {
        StockPricePersisterImpl(database = get())
    }
}