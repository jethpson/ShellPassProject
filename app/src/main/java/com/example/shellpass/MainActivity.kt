package com.example.shellpass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var urlInput: EditText
    private lateinit var openButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlInput = findViewById(R.id.editTextUrl)
        openButton = findViewById(R.id.buttonOpen)

        openButton.setOnClickListener {
            val enteredUrl = urlInput.text.toString().trim()
            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra("url", enteredUrl)
            }
            startActivity(intent)
        }
    }
}