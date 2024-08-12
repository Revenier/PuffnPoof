package com.example.puffnpoof.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.Model.userModel
import com.example.puffnpoof.R

class RegisterPage : AppCompatActivity() {

    private lateinit var dbHelper: dbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)
        supportActionBar?.hide()

        val Login_textButton = findViewById<TextView>(R.id.Loginhere)
        Login_textButton.setOnClickListener {
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }

        val RegisterButton = findViewById<TextView>(R.id.RegisterButton)
        RegisterButton.setOnClickListener {
            val emailRegis = findViewById<EditText>(R.id.SignUpEmail).text.toString()
            val usernameRegis = findViewById<EditText>(R.id.SignUpUsername).text.toString()
            val passwordRegis = findViewById<EditText>(R.id.SignUpPassword).text.toString()
            val phoneRegis = findViewById<EditText>(R.id.SignUpPhone).text.toString()
            val gender = findViewById<RadioGroup>(R.id.gender)

            dbHelper = dbHelper(this)

            if (emailRegis.isEmpty() || usernameRegis.isEmpty() || passwordRegis.isEmpty() || phoneRegis.isEmpty()) {
                Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!emailRegis.endsWith("@puff.com")) {
                Toast.makeText(this, "Email must end with @puff.com", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // username must be unique
            if (passwordRegis.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // ID di increment
            if (phoneRegis.length !in 11..13 || !TextUtils.isDigitsOnly(phoneRegis)) {
                Toast.makeText(this, "Phone Number must be between 11-13 digits!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val existingUser = dbHelper.getUsers().find { it.username == usernameRegis }
            if (existingUser != null) {
                Toast.makeText(this, "Username has already been used. Try another username.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val genderGroup = gender
            var selectedGender = ""
            val selectedRadioButtonId = genderGroup.checkedRadioButtonId
            selectedGender = when (selectedRadioButtonId) {
                R.id.GenderMale -> "Male"
                R.id.GenderFemale -> "Female"
                else -> ""
            }

            dbHelper.insertUsers(userModel( 0, usernameRegis, passwordRegis, emailRegis, phoneRegis, selectedGender))

            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
        }
    }
}
