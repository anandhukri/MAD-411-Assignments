data class Expense(
    val name: String,
    val amount: Double,
    val date: String,
    val currency: Currency,
    var convertedCost: Double )
