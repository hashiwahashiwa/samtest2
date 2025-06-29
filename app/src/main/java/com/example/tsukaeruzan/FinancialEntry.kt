package com.example.tsukaeruzan

data class FinancialEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    var name: String = "",
    var amount: Int = 0
) {
    fun toJson(): String {
        return "$id|$name|$amount"
    }

    companion object {
        fun fromJson(json: String): FinancialEntry? {
            return try {
                val parts = json.split("|")
                if (parts.size == 3) {
                    FinancialEntry(
                        id = parts[0],
                        name = parts[1],
                        amount = parts[2].toInt()
                    )
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}