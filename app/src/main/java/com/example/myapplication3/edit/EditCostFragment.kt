package com.example.myapplication3.edit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_cost.*


class EditCostFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_cost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suid = FirebaseAuth.getInstance().currentUser!!.uid
        val srootRef = FirebaseDatabase.getInstance().reference
        val suidRef = srootRef.child("postage").child(suid)
        val seventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val propNameTxt = dataSnapshot.child("volumetric").value.toString()
                val propCity = dataSnapshot.child("city").value.toString()
                val propState = dataSnapshot.child("state").value.toString()
                val propStreet = dataSnapshot.child("street").value.toString()
                val propPostcode = dataSnapshot.child("postcode").value.toString()
                val propCost = dataSnapshot.child("postagecost").value.toString()
                val propRename = dataSnapshot.child("recipientname").value.toString()
                val propRephone = dataSnapshot.child("recipientphone").value.toString()
                val propDate = dataSnapshot.child("date").value.toString()
                val propTime = dataSnapshot.child("time").value.toString()
                val propPostService = dataSnapshot.child("postageservice").value.toString()
                val propReferenceid = dataSnapshot.child("referenceid").value.toString()
                val proplength= dataSnapshot.child("length").value.toString()
                val propwidth = dataSnapshot.child("width").value.toString()
                val propheight = dataSnapshot.child("height").value.toString()
                length.text=proplength
                width.text=propwidth
                height.text=propheight
                storecitytown.text = propCity
                storestate.text = propState
                storestreet.text = propStreet
                storepostcode.text = propPostcode
                storecost.text = propCost
                storeName.text = propRename
                storeNumber.text = propRephone
                storeDate.text = propDate
                storeTime.text = propTime
                storeservice.text = propPostService
                storereference.text = propReferenceid
                volumetric.text=propNameTxt
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        suidRef.addListenerForSingleValueEvent(seventListener)

        postage_show.setOnClickListener {
            if (inputvolume.text.toString().trim().isEmpty()) {
                inputvolume.error = "Info Required"
                inputvolume.requestFocus()
                return@setOnClickListener
            }
            doCalculation()
        }
        count.setOnClickListener {
            if (inputvolume.text.toString().trim().isEmpty()) {
                inputvolume.error = "Info Required"
                inputvolume.requestFocus()
                return@setOnClickListener
            }
            saveToDatabase()
            val action = EditCostFragmentDirections.actionBackToEdit()
            Navigation.findNavController(it).navigate(action)
        }

    }
    private fun saveToDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/postage/$uid")
        val user = CUser(
            uid,
            length.text.toString(),
            width.text.toString(),
            height.text.toString(),
            volumetric.text.toString(),
            storeNumber.text.toString(),
            storeName.text.toString(),
            storecitytown.text.toString(),
            storestate.text.toString(),
            storepostcode.text.toString(),
            storestreet.text.toString(),
            costresult.text.toString(),
            storeservice.text.toString(),
            storereference.text.toString(),
            storeDate.text.toString(),
            storeTime.text.toString()

        )
        ref.setValue(user)
    }
    @SuppressLint("SetTextI18n")
    private fun doCalculation(){
        costresult.text = "Postage Cost: RM " + (inputvolume.text.toString().toDouble() * 0.8 )
    }

}
class CUser(val uid:String, val length:String, val width:String, val height:String,val volumetric:String, val recipientphone:String, val recipientname:String, val city:String,
            val state:String, val postcode: String, val street:String, val postagecost:String, val postageservice:String, val referenceid:String,
            val date:String, val time:String)
