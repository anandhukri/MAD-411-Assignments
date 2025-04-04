import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment7.QuoteApiService
import com.example.assignment7.R
import com.example.assignment7.RetrofitInstance
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var checkboxConversionNeeded: CheckBox
    private lateinit var spinnerCurrency: Spinner
    private lateinit var textViewConvertedCost: TextView
    private lateinit var expense: Expense
    private lateinit var QuoteApiService: QuoteApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkboxConversionNeeded = findViewById(R.id.checkboxConversionNeeded)
        spinnerCurrency = findViewById(R.id.spinnerCurrency)
        textViewConvertedCost = findViewById(R.id.textViewConvertedCost)

        val currencies = listOf("CAD", "EUR", "ISK")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        spinnerCurrency.adapter = adapter

        expense = Expense(amount = 100.0, currency = Currency("CAD", "$"))

        QuoteApiService = RetrofitInstance.QuoteApiService

        checkboxConversionNeeded.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                fetchExchangeRates()
            } else {
                textViewConvertedCost.text = "Converted Cost: $0.00"
            }
        }

        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (checkboxConversionNeeded.isChecked) {
                    fetchExchangeRates()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun fetchExchangeRates() {
        lifecycleScope.launch {
            try {
                val response = QuoteApiService.getExchangeRates("CAD")
                val selectedCurrencyCode = spinnerCurrency.selectedItem.toString()
                val exchangeRate = response.rates[selectedCurrencyCode] ?: 1.0
                val convertedCost = expense.amount * exchangeRate
                expense.convertedCost = convertedCost
                textViewConvertedCost.text = "Converted price: ${expense.currency.symbol}${"%.2f".format(convertedCost)}"
            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Failed to  exchange rate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
