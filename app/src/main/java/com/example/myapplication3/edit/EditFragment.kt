package com.example.myapplication3.edit

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
import kotlinx.android.synthetic.main.fragment_edit.*



class EditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_edit, container, false)
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
                val propPostService=dataSnapshot.child("postageservice").value.toString()
                val propReferenceid = dataSnapshot.child("referenceid").value.toString()
                displayEditVolumetric.text = propNameTxt
                editInfoCity.text = propCity
                editInfoState.text= propState
                editInfoStreet.text = propStreet
                editInfoPostcode.text=propPostcode
                displayEditCost.text=propCost
                editInfoName.text=propRename
                editInfoPhone.text=propRephone
                editInfoDate.text=propDate
                editInfoTime.text=propTime
                editInfoService.text=propPostService
                editInfoReference.text=propReferenceid
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        suidRef.addListenerForSingleValueEvent(seventListener)

        editVolumetric.setOnClickListener{
            val action=EditFragmentDirections.actionEditVolumetric()
            Navigation.findNavController(it).navigate(action)
        }
        editInfo.setOnClickListener{
            val action=EditFragmentDirections.actionToEditInfoDetail()
            Navigation.findNavController(it).navigate(action)
        }
    }
}


