package com.ianluong.newsbreak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var openButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openButton = findViewById(R.id.open_button)

        openButton.setOnClickListener {
            Toast.makeText(this, R.string.open_button_toast, Toast.LENGTH_SHORT).show()
        }
    }

}