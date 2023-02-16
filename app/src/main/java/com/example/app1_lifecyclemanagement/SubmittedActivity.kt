package com.example.app1_lifecyclemanagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SubmittedActivity : AppCompatActivity() {

    private lateinit var fullnameTV : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submitted)

        fullnameTV = findViewById(R.id.full_name_tv)

        val receivedIntent = intent

        //Get the string data
        fullnameTV.text = receivedIntent.getStringExtra("FULL_NAME")
    }


}