package com.example.myapplication3.account

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.home.HomeScreen
import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.login_screen.*


class LoginScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        auth = FirebaseAuth.getInstance()

        forgot_pass.setOnClickListener {
            startActivity(Intent(this, ResetPassword::class.java))
            finish()
        }

        sign_up_small.setOnClickListener{
            startActivity(Intent(this, SignUpScreen::class.java))
            finish()
        }
        sign_in.setOnClickListener {
            doLogin()
        }

    }

    private fun doLogin() {
        if (login_email.text.toString().isEmpty()) {
            login_email.error = "Please enter email"
            login_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(login_email.text.toString()).matches()) {
            login_email.error = "Please enter valid email"
            login_email.requestFocus()
            return
        }
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(login_email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val user:FirebaseUser?= auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Login Failed or verified Email Address.",Toast.LENGTH_SHORT).show()
                     updateUI(null)
                    
                }
            }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser:FirebaseUser?=auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser: FirebaseUser?) {
            if(currentUser !=null) {
                if(currentUser.isEmailVerified){
                    startActivity(Intent(this,
                        HomeScreen::class.java))
                    finish()
                }
                else {
                     Toast.makeText(baseContext, "Please verify your email address.",Toast.LENGTH_SHORT).show()
                }
            } /*else {
               Toast.makeText(baseContext, "Login Failed.",Toast.LENGTH_SHORT).show()
            }*/
    }
}