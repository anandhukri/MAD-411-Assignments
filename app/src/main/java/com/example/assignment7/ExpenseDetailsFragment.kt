package com.example.assignment7

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExpenseDetailsFragment : Fragment() {

    private var expenseName: String? = null
    private var expenseAmount: String? = null
    private var expenseDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            expenseName = it.getString("expenseName")
            expenseAmount = it.getString("expenseAmount")
            expenseDate = it.getString("expenseDate")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expense_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.exName)
        val amountTextView = view.findViewById<TextView>(R.id.exAmount)
        val dateView = view.findViewById<TextView>(R.id.exDate)

        nameTextView.text = "Expense Name: $expenseName"
        amountTextView.text = "Expense Amount: $expenseAmount"
        dateView.text = "Expense Date: $expenseDate"
    }

    companion object {
        @JvmStatic
        fun newInstance(name: String, amount: String, date: String) =
            ExpenseDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString("expenseName", name)
                    putString("expenseAmount", amount)
                    putString("expenseDate", date)
                }
            }
    }
}