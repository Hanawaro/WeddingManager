package com.weddingManager.weddingmanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.weddingManager.weddingmanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener {
            _, destination, _ -> when (destination.id) {
                R.id.menu -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(false);
                    supportActionBar?.setDisplayShowHomeEnabled(false);
                }
                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true);
                    supportActionBar?.setDisplayShowHomeEnabled(true);
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}