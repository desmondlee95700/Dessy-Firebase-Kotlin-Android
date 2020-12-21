package com.example.myapplication3.contactus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication3.R
import com.example.myapplication3.util.toast
import kotlinx.android.synthetic.main.fragment_contact_us.*


class ContactUsFragment : Fragment() {
    private val REQUEST_CALL = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sendmailto.setOnClickListener {
            val action = ContactUsFragmentDirections.actionToSendEmail()
            Navigation.findNavController(it).navigate(action )
        }
        sendphoneto.setOnClickListener{
            makePhoneCall()
        }
    }
    private fun makePhoneCall(){
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf<String>(Manifest.permission.CALL_PHONE), REQUEST_CALL);
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:03-41450123")
            startActivity(callIntent)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                context?.toast("PERMISSION DENIED")
            }
        }
    }
}