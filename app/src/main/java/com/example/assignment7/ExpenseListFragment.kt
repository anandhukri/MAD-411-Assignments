package com.example.assignment7

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

private const val FILE_NAME = "expenses.txt"

class ExpenseListFragment : Fragment() {
    private lateinit var editTextExpenseName: EditText
    private lateinit var editTextAmount: EditText
    private lateinit var buttonAddExpense: Button
    private lateinit var recyclerViewExpenses: RecyclerView
    private lateinit var textViewDate: TextView
    private val expenseList = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)
        initializeViews(view)
        setupRecyclerView()
        setupClickListeners()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.post {
            loadExpenses()
            updateTotalExpenseWithRetry()
        }
    }

    override fun onPause() {
        super.onPause()
        saveExpenses()
    }

    private fun initializeViews(view: View) {
        editTextExpenseName = view.findViewById(R.id.editTextName)
        editTextAmount = view.findViewById(R.id.editTextAmount)
        buttonAddExpense = view.findViewById(R.id.buttonAddExpense)
        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses)
        textViewDate = view.findViewById(R.id.textViewDate)
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter(expenseList) { position ->
            deleteExpense(position)
        }
        recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExpenses.adapter = expenseAdapter
    }

    private fun setupClickListeners() {
        buttonAddExpense.setOnClickListener { addExpense() }
        textViewDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                textViewDate.text = "$day/${month + 1}/$year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun addExpense() {
        val name = editTextExpenseName.text.toString().trim()
        val amount = editTextAmount.text.toString().trim()
        val date = textViewDate.text.toString().trim()

        if (name.isNotEmpty() && amount.isNotEmpty() && isValidAmount(amount)) {
            expenseList.add(Expense(name, amount, date))
            expenseAdapter.notifyItemInserted(expenseList.size - 1)
            editTextExpenseName.text.clear()
            editTextAmount.text.clear()
            saveExpenses()
            updateTotalExpense()
            Log.d("ExpenseList", "Added expense: $name, $amount, $date")
        } else {
            Toast.makeText(requireContext(),
                "Please enter valid name and amount",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteExpense(position: Int) {
        expenseList.removeAt(position)
        expenseAdapter.notifyItemRemoved(position)
        saveExpenses()
        updateTotalExpense()
        Log.d("ExpenseList", "Deleted expense at position $position")
    }

    private fun updateTotalExpense() {
        val total = expenseList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
        Log.d("ExpenseList", "Calculated total: $total")

        (activity as? MainActivity)?.let { activity ->
            val footer = activity.supportFragmentManager
                .findFragmentById(R.id.footer_container) as? FooterFragment
            footer?.updateTotalAmount(total) ?: run {
                Log.e("ExpenseList", "Footer fragment not found on first attempt")
            }
        }
    }

    private fun updateTotalExpenseWithRetry(maxRetries: Int = 3, delay: Long = 100L) {
        val total = expenseList.sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

        (activity as? MainActivity)?.let { activity ->
            val footer = activity.supportFragmentManager
                .findFragmentById(R.id.footer_container) as? FooterFragment

            if (footer != null && footer.isAdded) {
                footer.updateTotalAmount(total)
                Log.d("ExpenseList", "Successfully updated footer total")
            } else if (maxRetries > 0) {
                Log.d("ExpenseList", "Footer not ready - retrying ($maxRetries left)")
                view?.postDelayed(
                    { updateTotalExpenseWithRetry(maxRetries - 1, delay * 2) },
                    delay
                )
            } else {
                Log.e("ExpenseList", "Max retries reached - footer update failed")
            }
        }
    }

    private fun isValidAmount(amount: String): Boolean {
        return amount.toDoubleOrNull()?.let { it > 0 } ?: false
    }

    private fun saveExpenses() {
        try {
            val json = Gson().toJson(expenseList)
            requireContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses", e)
        }
    }

    private fun loadExpenses() {
        try {
            val file = File(requireContext().filesDir, FILE_NAME)
            if (!file.exists()) return

            val json = file.readText()
            val type = object : TypeToken<List<Expense>>() {}.type
            val loadedExpenses: List<Expense> = Gson().fromJson(json, type)

            expenseList.clear()
            expenseList.addAll(loadedExpenses)
            expenseAdapter.notifyDataSetChanged()

            Log.d("ExpenseList", "Loaded ${expenseList.size} expenses")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "Expenses file not found", e)
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading expenses", e)
        }
    }
}