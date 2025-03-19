package com.example.assignment7

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var editTextExpenseName: EditText
    lateinit var editTextAmount: EditText
    lateinit var buttonAddExpense: Button
    lateinit var recyclerViewExpenses: RecyclerView
    lateinit var textViewDate: TextView  // Added TextView for Date

    private val expenseList = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextExpenseName = findViewById(R.id.editTextName)
        editTextAmount = findViewById(R.id.editTextAmount)
        buttonAddExpense = findViewById(R.id.buttonAddExpense)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        textViewDate = findViewById(R.id.textViewDate)  // Initialize TextView

        expenseAdapter = ExpenseAdapter(expenseList) { position -> deleteExpense(position) }

        recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        recyclerViewExpenses.adapter = expenseAdapter

        buttonAddExpense.setOnClickListener { addExpense() }

        textViewDate.setOnClickListener { showDatePicker() }  // Set DatePickerDialog
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textViewDate.text = selectedDate  // Display selected date in TextView
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun addExpense() {
        val name = editTextExpenseName.text.toString().trim()
        val amount = editTextAmount.text.toString().trim()
        val date = textViewDate.text.toString().trim()

        if (name.isNotEmpty() && amount.isNotEmpty() && isValidAmount(amount)) {
            expenseList.add(Expense(name, amount, date))  // Pass date along with name and amount
            expenseAdapter.notifyItemInserted(expenseList.size - 1)
            editTextExpenseName.text.clear()
            editTextAmount.text.clear()

        } else {
            Toast.makeText(this, "Please enter valid name and amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteExpense(position: Int) {
        expenseList.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
    }

    private fun isValidAmount(amount: String): Boolean {
        return amount.toDoubleOrNull() != null && amount.toDouble() > 0
    }
}
