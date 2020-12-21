package com.example.myapplication3.contactus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.myapplication3.R
import kotlinx.android.synthetic.main.fragment_email.*

class EmailFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendEmailBtn.setOnClickListener{

            var recipient = recipientEt.text.toString().trim()
            var subject=subjectEt.text.toString().trim()
            var message= messageEt.text.toString().trim()

            sendEmail(recipient,subject,message)
        }
    }
    private fun sendEmail(recipient: String, subject: String, message: String) {

        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"

        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT,subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)
        try{

            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

}


