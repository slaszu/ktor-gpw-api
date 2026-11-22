package pl.slaszu.database.api

import kotlinx.serialization.Serializable

@Serializable
data class StockDTO(
    val code: String?,
    val name: String?
) {
    fun isValid(): Boolean {
        return !code.isNullOrBlank() || !name.isNullOrBlank()
    }

}