package com.example.myapplication3.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.braintreepayments.cardform.view.CardForm

import com.example.myapplication3.R
import com.example.myapplication3.util.login
import com.example.myapplication3.util.toast
import com.google.firebase.auth.FirebaseAuth

class PaymentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val t =inflater.inflate(R.layout.fragment_payment, container, false)
        val cardForm  = t.findViewById<CardForm>(R.id.card_form)
        val buy = t.findViewById<Button>(R.id.btnBuy)
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("SMS is required on this number")
            .setup(requireActivity());

          buy.setOnClickListener {
              if(cardForm.isValid) {
                  AlertDialog.Builder(requireActivity()).apply{
                      setTitle("Confirm before purchase")
                      setMessage("Card number: " + cardForm.cardNumber + "\n" +
                              "Card expiry date: " + cardForm.expirationDateEditText.text.toString() + "\n" +
                              "Card CVV: " + cardForm.cvv+ "\n" +
                              "Postal code: " + cardForm.postalCode + "\n" +
                              "Phone number: " + cardForm.mobileNumber
                      )
                      setPositiveButton("Confirm"){ _, _ ->
                          context?.toast("Thank you")
                          val action=PaymentFragmentDirections.actionPaymentHome()
                          Navigation.findNavController(it).navigate(action)

                      }
                      setNegativeButton("Cancel"){ _, _ ->
                          context?.toast("Please complete the form.")
                      }
                  }.create().show()
              }

          }
        // Inflate the layout for this fragment
        return t
    }


}
