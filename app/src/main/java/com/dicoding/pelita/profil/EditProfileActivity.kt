package com.dicoding.pelita.profil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelita.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextAlamat: TextInputEditText
    private lateinit var imageViewProfile: ImageView

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize FirebaseAuth, DatabaseReference, and StorageReference
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid ?: "")
        storageReference = FirebaseStorage.getInstance().reference.child("profile_images")

        // Initialize your TextInputEditTexts and ImageView
        editTextName = findViewById(R.id.editTextName)
        editTextPassword = findViewById(R.id.et_passwordLogin)
        editTextAlamat = findViewById(R.id.editTextAlamat)
        imageViewProfile = findViewById(R.id.iv_profile)

        // Find the "Simpan" button and ImageView for image upload
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnUploadImage: Button = findViewById(R.id.btn_unggah)

        // Set a click listener for the "Simpan" button
        btnSave.setOnClickListener {
            // Get the edited data from the TextInputEditTexts
            val nama = editTextName.text.toString()
            val telepon = editTextPassword.text.toString()
            val alamat = editTextAlamat.text.toString()

            // Save the edited data to Firebase Realtime Database
            val updatedUserData = HashMap<String, Any>()
            updatedUserData["nama"] = nama
            updatedUserData["telepon"] = telepon
            updatedUserData["alamat"] = alamat

            databaseReference.updateChildren(updatedUserData)
                .addOnSuccessListener {
                    // Data updated successfully
                    // You can add a Toast or any other UI feedback here if needed
                    setResult(Activity.RESULT_OK)
                    // Finish the activity and navigate back to the ProfileFragment
                    finish()
                }
                .addOnFailureListener { e ->
                    // Handle the failure to update data
                    // You can add a Toast or any other UI feedback here if needed
                }
        }

        // Set a click listener for the "Unggah Foto" button
        btnUploadImage.setOnClickListener {
            // Open the image picker
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // showing the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Handle the result from the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageViewProfile.setImageURI(imageUri)
            uploadImage()
        }
    }

    // Upload the selected image to Firebase Storage
    private fun uploadImage() {
        if (imageUri != null) {
            val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    // Image uploaded successfully, get the download URL
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Save the download URL to the Realtime Database
                        databaseReference.child("imageUri").setValue(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the failure to upload the image
                    // You can add a Toast or any other UI feedback here if needed
                    Log.e("EditProfileActivity", "Error uploading image: ${e.message}")
                }
        }
    }

    // this event will enable the back function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
