package com.example.puffnpoof.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.R

class LoginPage : AppCompatActivity() {

    private lateinit var dbHelper: dbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)
        supportActionBar?.hide()

        dbHelper = dbHelper(this)

        Log.d("dbHelper", "Fetched ${logAllUsers()} users")

//        deleteAllUsers()

        val registerText= findViewById<TextView>(R.id.SignUpHere)
        registerText.setOnClickListener {
            val intent = Intent(this, RegisterPage::class.java)
            startActivity(intent)
        }

        val loginButton= findViewById<Button>(R.id.LoginButton)
        loginButton.setOnClickListener {

            val usernameLogin = findViewById<EditText>(R.id.SignInUsername).text.toString()
            val passwordLogin = findViewById<EditText>(R.id.SignInPassword).text.toString()

            if (usernameLogin.isEmpty() && passwordLogin.isEmpty()){
                Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (usernameLogin.isEmpty()){
                Toast.makeText(this, "Username must be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (passwordLogin.isEmpty()){
                Toast.makeText(this, "Password must be filled!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = dbHelper.getUsers().find { it.username == usernameLogin && it.password == passwordLogin }

            if (user != null) {
                val intent = Intent(this, OTP_Page::class.java)
                intent.putExtra("currentUserID", user.userID )
                intent.putExtra("PhoneNumber", user.phoneNumber )
                Log.d("dbHelper", "Currrent : UserID ${user.userID} , Username ${user.username}")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }
    }

    private fun logAllUsers() {
        val users = dbHelper.getUsers()
        for (user in users) {
            Log.d("dbHelper","UserID: ${user.userID}, User: ${user.username}, Email: ${user.email}, Phone: ${user.phoneNumber}, Gender: ${user.gender}")
        }
    }

//    private fun deleteAllUsers() {
//        val rowsDeleted = dbHelper.deleteAllUsers()
//        if (rowsDeleted > 0) {
//            Toast.makeText(this, "$rowsDeleted users deleted.", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "No users to delete.", Toast.LENGTH_SHORT).show()
//        }
//        Log.d("dbHelper", "$rowsDeleted users deleted")
//    }
}