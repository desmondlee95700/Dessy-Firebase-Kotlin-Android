package com.example.myapplication3.account

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.R
import com.example.myapplication3.util.login
import com.example.myapplication3.util.logout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.sign_up_screen.*
import java.util.*


class SignUpScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_screen)
        auth = FirebaseAuth.getInstance()

        already_hav.setOnClickListener {
            val intent = Intent(
                this,
                LoginScreen::class.java
            )
            startActivity(intent)
        }

        register.setOnClickListener {
            signUpUser()
        }
    }


    private fun signUpUser() {
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }
        if (password.length() < 6){
            Toast.makeText(applicationContext,"Password too short, enter mimimum 6 charcters" , Toast.LENGTH_LONG).show()
            return signUpUser()
            }
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user=auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                saveUserToFirebaseDatabase()
                                AlertDialog.Builder(this).apply{
                                    setTitle("Please verify your Email Address")
                                    setPositiveButton("Ok"){ _, _ ->
                                        FirebaseAuth.getInstance().signOut()
                                        login()
                                    }
                                    /*setNegativeButton("Cancel"){ _, _ ->}*/
                                }.create().show()
                                /*Toast.makeText(baseContext, "Please verify your Email.",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,
                                    LoginScreen::class.java))
                                finish()*/
                            }
                        }

                } else {
                    Toast.makeText(baseContext, "Email has already been taken",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun saveUserToFirebaseDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            email.text.toString()
        )
        ref.setValue(user)
    }

}

class User(val uid:String, val email:String )