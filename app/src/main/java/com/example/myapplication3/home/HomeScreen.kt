package com.example.myapplication3.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.myapplication3.R
import com.example.myapplication3.databinding.HomeScreenBinding
import com.example.myapplication3.util.logout
import com.google.firebase.auth.FirebaseAuth


class HomeScreen : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<HomeScreenBinding>(this,
            R.layout.home_screen
        )

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.options_menu, menu)
        return true
        }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.action_logout){
            AlertDialog.Builder(this).apply{
                setTitle("Are you sure ?")
                setPositiveButton("Yes"){ _, _ ->
                   FirebaseAuth.getInstance().signOut()
                    logout()
                }
                setNegativeButton("Cancel"){ _, _ ->}
            }.create().show()
        }
        return super.onOptionsItemSelected(item)
    }


}
