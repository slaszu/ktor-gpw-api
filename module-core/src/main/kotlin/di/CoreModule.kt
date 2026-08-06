package pl.slaszu.core.di

import org.koin.dsl.bind
import org.koin.dsl.module
import pl.slaszu.core.api.StockRepository
import pl.slaszu.core.internal.repository.ExposedStockRepository
import pl.slaszu.core.api.DatabaseInitializer
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

val coreModule = module {
    single<Database> {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1" // lub do pliku: "jdbc:h2:./data/db"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }

    single { DatabaseInitializer(db = get()) }
    
    single<StockRepository> { 
        ExposedStockRepository(db = get()) 
    }
}