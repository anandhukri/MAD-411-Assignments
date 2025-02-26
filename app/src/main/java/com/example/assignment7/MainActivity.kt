package com.example.assignment7

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
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


        nameEdit = findViewById(R.id.nameEdit)
        result = findViewById(R.id.result)

    }

    fun display(view: View){
        val name = nameEdit.text.toString()
        result.text = "Hello $name!"
    }
}