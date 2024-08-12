package com.example.puffnpoof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.puffnpoof.Activity.LoginPage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val intent = Intent(this, LoginPage::class.java)
//        val intent = Intent(this, BottomNavBar::class.java)
        startActivity(intent)
        finish()
    }
}