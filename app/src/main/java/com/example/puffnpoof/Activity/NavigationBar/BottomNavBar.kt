package com.example.puffnpoof.Activity.NavigationBar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.puffnpoof.R
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import profile
import transaction

class BottomNavBar : AppCompatActivity() {
    private var curr_user_id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav_bar)
        supportActionBar?.hide()

        curr_user_id = intent.getIntExtra("currentUserID", 0)

        var BotNavBar = findViewById<ChipNavigationBar>(R.id.bottom_nav_bar)
        BotNavBar.setItemSelected(R.id.homeBNB, true)
        currentPage(home(), curr_user_id)

        BotNavBar.setOnItemSelectedListener {
            when(it){
                R.id.homeBNB -> {
                    val homeFragment = home()
                    val bundle = Bundle()
                    bundle.putInt("currentUserID", curr_user_id)
                    homeFragment.arguments = bundle
                    currentPage(homeFragment, curr_user_id)
                }
                R.id.TransactionBNB -> {
                    val bundle = Bundle()
                    bundle.putInt("currentUserID", curr_user_id)
                    val transactionFragment = transaction()
                    currentPage(transactionFragment, curr_user_id)
                }
                R.id.ProfieBNB -> {
                    val bundle = Bundle()
                    bundle.putInt("currentUserID", curr_user_id)
                    val profileFragment = profile()
                    currentPage(profileFragment, curr_user_id)
                }
            }
        }
    }

    private fun currentPage(fragment: Fragment, userID: Int) {
        val bundle = Bundle()
        bundle.putInt("currentUserID", curr_user_id)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frag_container_nav, fragment)
            commit()
        }
    }
}
