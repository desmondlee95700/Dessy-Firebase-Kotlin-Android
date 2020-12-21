package com.example.myapplication3.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation

import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_edit_info.*
import kotlinx.android.synthetic.main.fragment_edit_info.text_result
import kotlinx.android.synthetic.main.fragment_edit_info.volumetric
import kotlinx.android.synthetic.main.fragment_postage_cost.*
import kotlin.reflect.typeOf


class EditInfoFragment : Fragment() {
    private val types = arrayOf("Pos Laju", "J&T Express", "DHL Commerce", "City Link")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val t=inflater.inflate(R.layout.fragment_edit_info, container, false)
        val spinner = t.findViewById<Spinner>(R.id.spinner)
        spinner?.adapter = ArrayAdapter(activity?.applicationContext, R.layout.support_simple_spinner_dropdown_item, types)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val suid = FirebaseAuth.getInstance().currentUser!!.uid
        val srootRef = FirebaseDatabase.getInstance().reference
        val suidRef = srootRef.child("postage").child(suid)
        val seventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val propNameTxt = dataSnapshot.child("volumetric").value.toString()
                val propCost = dataSnapshot.child("postagecost").value.toString()
                val propReferenceid = dataSnapshot.child("referenceid").value.toString()
                volumetric.text = propNameTxt
                postagecost.text=propCost
                referenceid.text=propReferenceid
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        suidRef.addListenerForSingleValueEvent(seventListener)

        pickTimeBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(requireActivity(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        pickDateBtn.setOnClickListener {

            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                dateTv.text = "$dayOfMonth/$month/$year"
            }, year, month, day)
            dpd.show()

        }


        save_as_dra.setOnClickListener {
            if(timeTv.text.toString().isEmpty()){
                timeTv.error= "Please Choose Your Time"
                timeTv.requestFocus()
                return@setOnClickListener
            }
            if(dateTv.text.toString().isEmpty()){
                dateTv.error= "Please Choose Your Date"
                dateTv.requestFocus()
                return@setOnClickListener
            }
            if(recipient_n.text.toString().trim().isEmpty()){
                recipient_n.error="Recipient Name Required"
                recipient_n.requestFocus()
                return@setOnClickListener
            }
            if(phone_number.text.toString().trim().isEmpty()) {
                phone_number.error="Recipient Phone Number Required"
                phone_number.requestFocus()
                return@setOnClickListener
            }
            if(street_address.text.toString().trim().isEmpty()){
                street_address.error="Stress Address Required"
                street_address.requestFocus()
                return@setOnClickListener
            }
            if(city_town.text.toString().isEmpty()){
                city_town.error="City/Town Required"
                city_town.requestFocus()
                return@setOnClickListener
            }
            if( stateinfo.text.toString().trim().isEmpty()){
                stateinfo.error="State Required"
                stateinfo.requestFocus()
                return@setOnClickListener
            }
            if(postcodeinfo.text.toString().trim().isEmpty()){
                postcodeinfo.error="Postcode Required"
                postcodeinfo.requestFocus()
                return@setOnClickListener
            }
            saveToDatabase()
            val action = EditInfoFragmentDirections.actionBackEdit2()
            Navigation.findNavController(it).navigate(action)


        }
    }
    private fun saveToDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/postage/$uid")
        val user = DUser(
            uid,
            length.text.toString(),
            width.text.toString(),
            height.text.toString(),
            volumetric.text.toString(),
            phone_number.text.toString(),
            recipient_n.text.toString(),
            city_town.text.toString(),
            stateinfo.text.toString(),
            postcodeinfo.text.toString(),
            street_address.text.toString(),
            postagecost.text.toString(),
            text_result.text.toString(),
            referenceid.text.toString(),
            dateTv.text.toString(),
            timeTv.text.toString()

        )
        ref.setValue(user)
    }


}

class DUser(val uid:String, val length:String, val width:String, val height:String,val volumetric:String, val recipientphone:String, val recipientname:String, val city:String,
            val state:String, val postcode: String, val street:String, val postagecost:String, val postageservice:String, val referenceid:String,
            val date:String, val time:String)
