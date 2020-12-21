package com.example.myapplication3.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myapplication3.R
import com.example.myapplication3.util.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {

    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"
    private lateinit var imageUri : Uri
    private val REQUEST_IMAGE_CAPTURE = 100
    private val MY_CAMERA_REQUEST_CODE = 100
    private val MY_GALLERY_REQUEST_CODE = 100
    private val IMAGE_PICK_CODE = 1

    private val currentUser=FirebaseAuth.getInstance().currentUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
        // Inflate the layout for this fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUser?.let { user ->
            Glide.with(this)
                .load(user.photoUrl)
                .into(image_view)
            name.setText(user.displayName)
            email_id.text = user.email
            text_phone.text = if (user.phoneNumber.isNullOrEmpty()) "Add Number" else user.phoneNumber

            if(user.isEmailVerified){
                text_not_verified.visibility=View.INVISIBLE
            }else{
                text_not_verified.visibility=View.VISIBLE
            }

        }
        image_view.setOnClickListener {
            /*takePictureIntent()*/
            showPictureDialog()
        }

        save.setOnClickListener {
            val photo = when {
                ::imageUri.isInitialized -> imageUri
                currentUser?.photoUrl == null -> Uri.parse(DEFAULT_IMAGE_URL)
                else -> currentUser.photoUrl
            }
            /*val name = name.text.toString().trim()*/
            if (name.text.toString().trim().isEmpty()) {
                name.error = "Please Enter Your Name"
                name.requestFocus()
                return@setOnClickListener
            }
            val updates = UserProfileChangeRequest.Builder()
                .setDisplayName(name.text.toString().trim())
                .setPhotoUri(photo)
                .build()

            progressbar_pic2.visibility = View.VISIBLE
            currentUser?.updateProfile(updates)
                ?.addOnCompleteListener{task ->
                    progressbar_pic2.visibility = View.INVISIBLE
                    if(task.isSuccessful){
                        context?.toast("Profile Updated")

                    }else {
                        context?.toast(task.exception?.message!!)
                    }
                }
            saveToDatabase()
        }
        text_phone.setOnClickListener{
            val action = ProfileFragmentDirections.actionVerifyPhone()
            Navigation.findNavController(it).navigate(action)
        }
        email_id.setOnClickListener{
            val action = ProfileFragmentDirections.actionUpdateEmail()
            Navigation.findNavController(it).navigate(action)
        }
        text_not_verified.setOnClickListener{
            currentUser?.sendEmailVerification()
                ?.addOnCompleteListener{
                    if(it.isSuccessful){
                        context?.toast("Verification Email Sent")
                    }else{
                        context?.toast(it.exception?.message!!)
                    }
                }
        }

    }
    private fun choosePhotoFromGallery() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), MY_GALLERY_REQUEST_CODE
            );
        } else {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE) }
        }

    private fun showPictureDialog() {
        val pictureDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf(
            "Select photo from gallery",
            "Capture photo from camera"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallery()
                    1 -> takePictureIntent()
                }
            })
        pictureDialog.show()
    }
    private fun takePictureIntent() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.CAMERA), MY_CAMERA_REQUEST_CODE
            );
        } else {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { pictureIntent ->
                pictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                    startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                context?.toast("PERMISSION GRANTED")
            } else {
                context?.toast("PERMISSION DENIED")
            }
        }
        else if(requestCode == MY_GALLERY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                context?.toast("PERMISSION GRANTED")
            } else {
                context?.toast("PERMISSION DENIED")
            }
        }
    }

    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){
            if (data != null) {
                selectedPhotoUri = data.data
            }
            val imageBitmap2 =  MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhotoUri) as Bitmap
            uploadImageAndSaveUri(imageBitmap2)

        }
         else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(imageBitmap)
        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference.child("images/${FirebaseAuth.getInstance().currentUser?.uid}")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        val upload = storageRef.putBytes(image)

        progressbar_pic.visibility=View.VISIBLE
        upload.addOnCompleteListener{uploadTask ->
            progressbar_pic.visibility=View.INVISIBLE
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener{urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        activity?.toast(imageUri.toString())
                        image_view.setImageBitmap(bitmap)
                    }
                }
            } else {
                uploadTask.exception?.let{
                    activity?.toast(it.message!!)
                }
            }
        }
    }

    private fun saveToDatabase() {
        val uid=FirebaseAuth.getInstance().uid ?: ""
        val ref= FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            name.text.toString(),
            text_phone.text.toString(),
            email_id.text.toString()
        )
        ref.setValue(user)
    }

}


class User(val uid:String, val name:String, val phone:String, val email_id:String )
