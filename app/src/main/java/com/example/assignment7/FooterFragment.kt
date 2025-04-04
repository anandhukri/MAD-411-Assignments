package com.example.assignment7

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FooterFragment : Fragment() {
    private lateinit var textViewTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("FooterFragment", "onCreateView called")
        return inflater.inflate(R.layout.fragment_footer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FooterFragment", "onViewCreated called")
        textViewTotal = view.findViewById(R.id.textViewTotalAmount)
    }


    fun updateTotalAmount(amount: Double) {
        Log.d("FooterFragment", "updateTotalAmount called with: $amount")
        if (::textViewTotal.isInitialized) {
            textViewTotal.text = "Total Expense: $%.2f".format(amount)
        } else {
            Log.e("FooterFragment", "TextView not initialized yet!")
        }
    }
}