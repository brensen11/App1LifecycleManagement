package com.example.app1_lifecyclemanagement

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var firstName : String? = null
    private var middleName : String? = null
    private var lastName : String? = null
    private var profilePic : Bitmap? = null

    private lateinit var firstNameTV : TextView
    private lateinit var middleNameTV : TextView
    private lateinit var lastNameTV : TextView
    private lateinit var picBT: Button
    private lateinit var submitBT : Button
    private lateinit var profileIV : ImageView


    private fun submit() {
        if(firstNameTV.text.isNullOrBlank() or lastNameTV.text.isNullOrBlank()) {
            val text = "Must enter first and last name!"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
            return
        }
        //Start an activity and pass the EditText string to it.
        // this is an explicit intent!
        val messageIntent = Intent(this, SubmittedActivity::class.java)
        val fullname = firstNameTV.text.toString() + " " + lastNameTV.text.toString()
        messageIntent.putExtra("FULL_NAME", fullname) // we can add information to the intent we're sending ...

        this.startActivity(messageIntent) // and then start the activity!
    }

    private fun takePicture() {
        Log.d("STATE","Taking Picture!")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            cameraActivity.launch(cameraIntent)
        }catch(ex: ActivityNotFoundException){
            //Do error handling here
        }
    }

    private val cameraActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 33) {
                val thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
                profileIV.setImageBitmap(thumbnailImage)
            } else {
                val thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                profileIV.setImageBitmap(thumbnailImage)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNameTV = findViewById(R.id.first_name_et)
        middleNameTV = findViewById(R.id.middle_name_et)
        lastNameTV = findViewById(R.id.last_name_et)
        picBT = findViewById(R.id.picture_bt)
        submitBT = findViewById(R.id.submit_bt)
        profileIV = findViewById(R.id.profile_iv)

        submitBT.setOnClickListener {
            submit()
        }

        picBT.setOnClickListener{
            takePicture()
        }
    }

    // save data here
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Get the strings
        firstName = firstNameTV.text.toString()
        middleName = middleNameTV.text.toString()
        lastName = lastNameTV.text.toString()
        profileIV.drawable?.let {
            profilePic = it.toBitmap()
        }


        //Put them in the outgoing Bundle. whack.
        outState.putString("FN_TEXT", firstName)
        outState.putString("MN_TEXT", middleName)
        outState.putString("LN_TEXT", lastName)
        outState.putParcelable("PF_IMAGE", profilePic)
    }

    // restore data here
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        //Restore the view hierarchy automatically
        super.onRestoreInstanceState(savedInstanceState)

        //Restore stuff
        firstNameTV.text = savedInstanceState.getString("FN_TEXT")
        middleNameTV.text = savedInstanceState.getString("MN_TEXT")
        lastNameTV.text = savedInstanceState.getString("LN_TEXT")

        if (Build.VERSION.SDK_INT >= 33) {
            profilePic = savedInstanceState.getParcelable("PF_IMAGE", Bitmap::class.java)
        } else {
            profilePic = savedInstanceState.getParcelable("PF_IMAGE")
        }

        profilePic?.let {
            profileIV.setImageBitmap(it)
        }
    }

}