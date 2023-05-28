package com.idodanieli.playit.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.idodanieli.playit.R
import com.idodanieli.playit.SharedPrefsManager


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameEditText = findViewById<EditText>(R.id.registerUsername)
        val registerButton = findViewById<Button>(R.id.registerButton)

        val sharedPrefsManager = SharedPrefsManager.getInstance()

        registerButton.setOnClickListener {
            sharedPrefsManager.setUsername(usernameEditText.text.toString())
            Toast.makeText(this, "Welcome!", Toast.LENGTH_LONG).show()
            finish()
        }
    }


}