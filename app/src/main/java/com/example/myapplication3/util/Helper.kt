package com.example.myapplication3.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.myapplication3.MainActivity
import com.example.myapplication3.account.LoginScreen
import com.example.myapplication3.home.HomeScreen
import com.example.myapplication3.profile.ProfileFragment

fun Context.toast(message: String) =
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()

fun Context.logout() {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
    }

fun Context.login(){
    val intent = Intent(this, LoginScreen::class.java)
    startActivity(intent)
}

fun Context.backhome() {
    val intent = Intent(this, HomeScreen::class.java)
    startActivity(intent)
}

fun Context.backprofile() {
    val intent = Intent(this, ProfileFragment::class.java)
    startActivity(intent)
}

