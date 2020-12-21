package com.example.myapplication3.postage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_postage_cost.*
import kotlinx.android.synthetic.main.fragment_postage_cost.volumetric



class PostageCostFragment : Fragment() {

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_postage_cost, container, false)
    }*/
    private val types = arrayOf("Pos Laju", "J&T Express", "DHL Commerce", "City Link")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val t=inflater.inflate(R.layout.fragment_postage_cost, container, false)
        val spinner = t.findViewById<Spinner>(R.id.spinner)
        spinner?.adapter = ArrayAdapter(activity?.applicationContext, R.layout.support_simple_spinner_dropdown_item, types) as SpinnerAdapter
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                /*val type = parent?.getItemAtPosition(position).toString()
                Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)*/
                text_result.text = parent?.getItemAtPosition(position).toString()

            }
        }
        return t
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
                    val propRename = dataSnapshot.child("recipientname").value.toString()
                    val propRephone = dataSnapshot.child("recipientphone").value.toString()
                    val propDate = dataSnapshot.child("date").value.toString()
                    val propTime = dataSnapshot.child("time").value.toString()
                    val proplength= dataSnapshot.child("length").value.toString()
                    val propwidth = dataSnapshot.child("width").value.toString()
                    val propheight = dataSnapshot.child("height").value.toString()
                    length_cm.text = proplength
                    width_cm.text= propwidth
                    height_cm.text = propheight
                    volumetric.text = propNameTxt
                    storecitytown.text = propCity
                    storestate.text= propState
                    storestreet.text = propStreet
                    storepostcode.text=propPostcode
                    storename.text=propRename
                    storenumber.text=propRephone
                    storedate.text = propDate
                    storetime.text = propTime

                }
                override fun onCancelled(databaseError: DatabaseError) {}
            }
            suidRef.addListenerForSingleValueEvent(seventListener)


        postage_show.setOnClickListener {
            if (input_volume.text.toString().trim().isEmpty()) {
                input_volume.error = "Info Required"
                input_volume.requestFocus()
                return@setOnClickListener
            }
            doCalculation()
        }
        saveToTrack.setOnClickListener {
            if (input_volume.text.toString().trim().isEmpty()) {
                input_volume.error = "Info Required"
                input_volume.requestFocus()
                return@setOnClickListener
            }
            saveToDatabase()
            val action = PostageCostFragmentDirections.actionPostageConfirm()
            Navigation.findNavController(it).navigate(action)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun doCalculation(){
        cost_result.text = "Postage Cost: RM " + (input_volume.text.toString().toDouble() * 0.8 )
    }
    private fun saveToDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/postage/$uid")
        val user = TUser(
            uid,
            length_cm.text.toString(),
            width_cm.text.toString(),
            height_cm.text.toString(),
            volumetric.text.toString(),
            storenumber.text.toString(),
            storename.text.toString(),
            storecitytown.text.toString(),
            storestate.text.toString(),
            storepostcode.text.toString(),
            storestreet.text.toString(),
            cost_result.text.toString(),
            text_result.text.toString(),
            storedate.text.toString(),
            storetime.text.toString()
        )
        ref.setValue(user)
    }


}
class TUser(val uid:String, val length:String, val width:String, val height:String, val volumetric:String, val recipientphone:String, val recipientname:String, val city:String,
val state:String, val postcode: String, val street:String, val postagecost:String, val postageservice:String, val date: String, val time: String)