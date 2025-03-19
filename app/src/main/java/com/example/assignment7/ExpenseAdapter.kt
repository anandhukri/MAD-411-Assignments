package com.example.assignment7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textExpenseName)
        val textAmount: TextView = itemView.findViewById(R.id.textExpenseAmount)
        val textDate: TextView = itemView.findViewById(R.id.textExpenseDate)  // Show date in item
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.textName.text = expense.name
        holder.textAmount.text = expense.amount
        holder.textDate.text = expense.date  // Display the date
        holder.buttonDelete.setOnClickListener { onDeleteClick(position) }
    }

    override fun getItemCount(): Int = expenses.size
}
