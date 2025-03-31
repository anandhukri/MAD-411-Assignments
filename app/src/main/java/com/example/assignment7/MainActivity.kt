package com.example.assignment7

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    lateinit var editTextExpenseName: EditText
    lateinit var editTextAmount: EditText
    lateinit var buttonAddExpense: Button
    lateinit var recyclerViewExpenses: RecyclerView

    lateinit var textViewDate: TextView

    private lateinit var footerFragment: FooterFragment
    private val expenseList = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.header_container, HeaderFragment())
            .commit()

        footerFragment = FooterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.footer_container, footerFragment)
            .commit()

        editTextExpenseName = findViewById(R.id.editTextName)
        editTextAmount = findViewById(R.id.editTextAmount)
        buttonAddExpense = findViewById(R.id.buttonAddExpense)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        textViewDate = findViewById(R.id.textViewDate)

        expenseAdapter = ExpenseAdapter(expenseList) { position -> deleteExpense(position) }

        recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        recyclerViewExpenses.adapter = expenseAdapter

        buttonAddExpense.setOnClickListener { addExpense() }

        textViewDate.setOnClickListener { showDatePicker() }

        val tipButton = findViewById<Button>(R.id.financeButton)

        tipButton.setOnClickListener {
            val url = "https://www.investopedia.com/articles/younginvestors/08/eight-tips.asp"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("ActivityLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textViewDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun addExpense() {
        val name = editTextExpenseName.text.toString().trim()
        val amount = editTextAmount.text.toString().trim()
        val date = textViewDate.text.toString().trim()

        loadToFile()

        if (name.isNotEmpty() && amount.isNotEmpty() && isValidAmount(amount)) {
            expenseList.add(Expense(name, amount, date))
            expenseAdapter.notifyItemInserted(expenseList.size - 1)
            updateTotalExpense()

            saveToFile(expenseList)
            editTextExpenseName.text.clear()
            editTextAmount.text.clear()


        } else {
            Toast.makeText(this, "Please enter valid name and amount", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteExpense(position: Int) {
        expenseList.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
        updateTotalExpense()
    }

    private fun updateTotalExpense() {
        val total = expenseList.sumOf { it.amount.toDouble() }
        footerFragment.updateTotalAmount(total)
    }

    private fun isValidAmount(amount: String): Boolean {
        return amount.toDoubleOrNull() != null && amount.toDouble() > 0
    }


    private fun saveToFile(expenses: List<Expense>){
        val gson = Gson()
        val json = gson.toJson(expenses)

        try {
            openFileOutput("expenses.json", MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
        }catch (e:Exception){
            Log.e("Main Activity","Error saving expenses",e)
        }
    }

    private fun loadToFile():List<Expense>{
        val expenses = mutableListOf<Expense>()
        try {
            openFileInput("expenses.json").use { input ->
                val json = input.bufferedReader().readText()
                val gson = Gson()
                val expenseListType = object : TypeToken<List<Expense>>() {}.type
                expenses.addAll(gson.fromJson(json,expenseListType))

            }
        }catch (e:Exception){
            Log.e("MainActivity","Error reading expenses",e)
        }
        return expenses
    }


}
