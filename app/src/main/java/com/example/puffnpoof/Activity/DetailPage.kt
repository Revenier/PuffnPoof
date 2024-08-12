package com.example.puffnpoof.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.puffnpoof.Database.dbHelper
import com.example.puffnpoof.Model.*
import com.example.puffnpoof.R
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class DetailPage : AppCompatActivity() {
    private lateinit var dbHelper: dbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        supportActionBar?.hide()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        val backButton = findViewById<ImageView>(R.id.backbutton)
        backButton.setOnClickListener {
            finish()
        }

        dbHelper= dbHelper(this)
        val boneka = intent.getSerializableExtra("doll") as dollModel
        val userId = intent.getIntExtra("currentUserID", 0)
        val curr_user = dbHelper.getUsers().find { it.userID==userId }

        if (curr_user != null) {
            Log.d("tag", "DI detail page : ${curr_user.username}")
        }

        val namaBoneka = findViewById<TextView>(R.id.Dollname)
        val size = findViewById<TextView>(R.id.size)
        val deskripsi = findViewById<TextView>(R.id.description)
        val harga = findViewById<TextView>(R.id.dollprice)
        val rating = findViewById<TextView>(R.id.rating)
        val gambar = findViewById<ImageView>(R.id.imageView)

        Picasso.get().load(boneka.imageLink).into(gambar)
        namaBoneka.text = boneka.name
        size.text = boneka.size.toString()
        deskripsi.text = boneka.description.toString()
        harga.text = "$" + boneka.price.toString()
        rating.text = boneka.rating.toString()

        val quantityEditText = findViewById<EditText>(R.id.quantity)
        val totalPriceTextView = findViewById<TextView>(R.id.dollprice)

        quantityEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val quantity = if (s.isNullOrEmpty()) 0 else s.toString().toInt()
                val totalPrice = quantity * boneka.price!!

                totalPriceTextView.text = "$" + totalPrice.toString()
            }
        })

        val buyButton = findViewById<Button>(R.id.buy_button)
        buyButton.setOnClickListener {
            val quantity = quantityEditText.text.toString().toIntOrNull()
            if (quantity != null) {

                val transaction = transactionModel(0, curr_user?.userID ?: 0, boneka.dollID, quantity, formatDate(
                    Date()
                ))
                Log.d("tag", "transID = ${transaction.transactionID}")
                Log.d("tag", "userID = ${transaction.userID}")
                Log.d("tag", "dollID = ${transaction.dollID}")
                Log.d("tag", "Jumlah ${transaction.transactionQuantity}")
                dbHelper.insertTransaction(transaction)

                if (curr_user != null) {
                    Log.d("tess","${dbHelper.getTransaction(curr_user.userID)}")
                }


                Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun formatDate(date: Date): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}
