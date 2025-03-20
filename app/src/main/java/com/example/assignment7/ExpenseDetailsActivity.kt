package com.example.assignment7

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expense_details)

        val name = intent.getStringExtra("expenseName")
        val amount = intent.getStringExtra("expenseAmount")
        val date = intent.getStringExtra("expenseDate")

        val nameTextView = findViewById<TextView>(R.id.exName)
        val amountTextView = findViewById<TextView>(R.id.exAmount)
        val dateView = findViewById<TextView>(R.id.exDate)

        nameTextView.text = "Expense Name: $name"
        amountTextView.text = "Expense Amount $amount"
        dateView.text = "Expense Date: $date"
    }
}