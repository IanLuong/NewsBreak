package com.ianluong.newsbreak

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SearchActivity : AppCompatActivity() {

    lateinit var openButton: Button
    lateinit var urlEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        urlEditText = findViewById(R.id.url_edit_text)

        openButton = findViewById(R.id.open_button)
        openButton.setOnClickListener {
            val website = Uri.parse(urlEditText.text.toString())
            val intent = Intent(Intent.ACTION_VIEW, website)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "URL not found\n Use https://...", Toast.LENGTH_SHORT).show()
            }
        }

    }



}