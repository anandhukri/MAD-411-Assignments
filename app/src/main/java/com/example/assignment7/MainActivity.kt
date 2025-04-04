package com.example.assignment7

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.header_container, HeaderFragment())
            .commitNow()

        supportFragmentManager.beginTransaction()
            .replace(R.id.footer_container, FooterFragment())
            .commitNow()



        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, _, _ ->
            setupActionBarWithNavController(navController)
        }


        findViewById<Button>(R.id.financeButton).setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.investopedia.com/articles/younginvestors/08/eight-tips.asp")))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}