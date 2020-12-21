package com.example.myapplication3.postage

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_postage.*

private val currentUser=FirebaseAuth.getInstance().currentUser

class PostageFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_postage, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            count.setOnClickListener {
                if(length_cm_.text.toString().isEmpty()){
                    length_cm_.error="Required Field"
                    length_cm_.requestFocus()
                    return@setOnClickListener
                }
                if(width_cm_.text.toString().isEmpty()){
                    width_cm_.error="Required Field"
                    width_cm_.requestFocus()
                   return@setOnClickListener
                }
                if(height_cm_.text.toString().isEmpty()){
                    height_cm_.error="Required Field"
                    height_cm_.requestFocus()
                    return@setOnClickListener
                }
                calculation()
                val action=PostageFragmentDirections.actionSubmitVolumetric()
                Navigation.findNavController(it).navigate(action)
                saveToDatabase()
            }

    }

    @SuppressLint("SetTextI18n")
    private fun calculation(){
        volumetric_result.text = "Volumetric Weight: " + (length_cm_.text.toString().toDouble() * width_cm_.text.toString()
                .toDouble() * height_cm_.text.toString().toDouble() / 5000) +"kg"
    }
    private fun saveToDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/postage/$uid")
        val user = User(
            uid,
            length_cm_.text.toString(),
            width_cm_.text.toString(),
            height_cm_.text.toString(),
            volumetric_result.text.toString()
        )
        ref.setValue(user)
    }
}
class User(val uid:String, val length:String, val width:String, val height:String, val volumetric:String )