import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.assignment7.QuoteApiService
import com.example.assignment7.R
import com.example.assignment7.RetrofitInstance

class ExpenseDetailsFragment : Fragment() {


    private lateinit var checkboxConversionNeeded: CheckBox
    private lateinit var spinnerCurrency: Spinner
    private lateinit var textViewConvertedCost: TextView
    private lateinit var expense: Expense
    private lateinit var QuoteApiService: QuoteApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expense_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkboxConversionNeeded = view.findViewById(R.id.checkboxConversionNeeded)
        spinnerCurrency = view.findViewById(R.id.spinnerCurrency)
        textViewConvertedCost = view.findViewById(R.id.textViewConvertedCost)

        val currencies = listOf("CAD", "EUR", "ISK")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
        spinnerCurrency.adapter = adapter

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
                textViewConvertedCost.text = "Converted Cost: ${expense.currency.symbol}${"%.2f".format(convertedCost)}"
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to fetch exchange rate", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
