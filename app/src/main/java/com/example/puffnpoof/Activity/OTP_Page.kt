package com.example.puffnpoof.Activity

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.puffnpoof.Activity.NavigationBar.BottomNavBar
import com.example.puffnpoof.R

class OTP_Page : AppCompatActivity() {

    private lateinit var smsManager: SmsManager
    private var curr_user_id: Int = 0
    private lateinit var phonenumber: String
    private var isCooldown = false
    private var OTP: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_page)
        supportActionBar?.hide()

        curr_user_id = intent.getIntExtra("currentUserID", 0)
        phonenumber = intent.getStringExtra("PhoneNumber") ?: ""

        Log.d("", "${curr_user_id}, ${phonenumber}")

        smsManager = SmsManager.getDefault()

        OTP = generateOtpCode()
        checkSendSMSPermission(phonenumber, OTP)

        Log.d("", "OTP :  ${OTP} ")

        val inputOTPField = findViewById<EditText>(R.id.input_otp)
        val resendCode = findViewById<TextView>(R.id.resend)
        val submitBtn = findViewById<Button>(R.id.submit_button)

        resendCode.setOnClickListener {
            if (!isCooldown) {
                OTP = generateOtpCode()
                checkSendSMSPermission(phonenumber, OTP)
                startCooldown(resendCode)
            } else {
                Toast.makeText(this, "Please wait 30 seconds before resending the OTP.", Toast.LENGTH_SHORT).show()
            }
        }

        submitBtn.setOnClickListener {
            val inputOTP = inputOTPField.text.toString()
            if (inputOTP == OTP) {
                val intent = Intent(this, BottomNavBar::class.java)
                intent.putExtra("currentUserID", curr_user_id)
                startActivity(intent)
            } else {
                Toast.makeText(this, "OTP incorrect! Please check again.", Toast.LENGTH_SHORT).show()
                inputOTPField.text.clear()
            }
        }
    }

    private fun generateOtpCode(): String {
        val otpLength = 4
        val possibleChars = "0123456789"
        return (1..otpLength)
            .map { possibleChars.random() }
            .joinToString("")
    }

    private fun checkSendSMSPermission(phonenumber: String, message: String) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), 100)
        } else {
            sendSMS(phonenumber, message)
        }
    }

    private fun sendSMS(phonenumber: String, message: String) {
        smsManager.sendTextMessage(phonenumber, null, message, null, null)
        Toast.makeText(this, "OTP sent to $phonenumber", Toast.LENGTH_SHORT).show()
    }

    private fun startCooldown(resendCode: TextView) {
        isCooldown = true
        resendCode.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({
            isCooldown = false
            resendCode.isEnabled = true
        }, 30000) // 30 seconds cooldown
    }
}
