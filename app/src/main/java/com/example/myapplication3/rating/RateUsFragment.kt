package com.example.myapplication3.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_rate_us.*


class RateUsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ratingStar.onRatingBarChangeListener = OnRatingBarChangeListener { ratingBar, v, b ->
            tvRatingScale.text = v.toString()
            tvRatingNumber.text = v.toString()
            when (ratingBar.rating.toInt()) {
                1 -> tvRatingScale.text = "Very bad"
                2 -> tvRatingScale.text = "Need some improvement"
                3 -> tvRatingScale.text = "Good"
                4 -> tvRatingScale.text = "Great"
                5 -> tvRatingScale.text = "Awesome. I love it"
                else -> tvRatingScale.text = ""
            }
            when (ratingBar.rating.toInt()) {
                1 -> tvRatingNumber.text = "Rating Score: 1.0"
                2 -> tvRatingNumber.text = "Rating Score: 2.0"
                3 -> tvRatingNumber.text = "Rating Score: 3.0"
                4 -> tvRatingNumber.text = "Rating Score: 4.0"
                5 -> tvRatingNumber.text = "Rating Score: 5.0"
                else -> tvRatingNumber.text = ""
            }
        }
        submitFeedback.setOnClickListener(View.OnClickListener {
            if (feedbacktext.text.toString().isEmpty()) {
                feedbacktext.error="Please Enter Your Feedback"
                feedbacktext.requestFocus()
                return@OnClickListener
            } else {
/*                feedbacktext.setText("")
                ratingStar.rating = 0F*/
                Toast.makeText(activity,"Thank you for sharing your feedback",Toast.LENGTH_SHORT).show();
            }
            saveToDatabase()
        })
    }
    private fun saveToDatabase() {
        val uid= FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/rate/$uid")
        val user = RUser(
            uid,
            tvRatingScale.text.toString(),
            feedbacktext.text.toString()
        )
        ref.setValue(user)
    }
}

class RUser(val uid:String, val rating:String, val feedback:String)
