package com.example.myapplication3.account

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.reset_password.*


class ResetPassword : AppCompatActivity() {

   private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)
        auth = FirebaseAuth.getInstance()

        login_small.setOnClickListener{
            val intent = Intent(this,
                LoginScreen::class.java)
            startActivity(intent)
        }
        reset.setOnClickListener{
            doReset()
        }
    }
    private fun doReset(){
        if(reset_email.text.toString().isEmpty()){
            reset_email.error = "Please enter email"
            reset_email.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(reset_email.text.toString()).matches()) {
            reset_email.error = "Please enter valid email"
            reset_email.requestFocus()
            return
        }
        auth.sendPasswordResetEmail(reset_email.text.toString())
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    Toast.makeText(baseContext, "Reset Password Send To Your Email.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(baseContext, "Not verified or invalid Email.",Toast.LENGTH_SHORT).show()
                }
            }
    }
}