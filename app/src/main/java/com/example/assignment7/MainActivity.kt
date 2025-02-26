package com.example.assignment7

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var nameEdit: EditText
    lateinit var result: TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        var nameEdit = findViewById<TextView>(R.id.nameBox)
        var result = findViewById<TextView>(R.id.showResult)
        var button = findViewById<Button>(R.id.submitButton)

        button.setOnClickListener {
            result.text = "Hello, ${nameEdit.text}!"

        }

    }


}