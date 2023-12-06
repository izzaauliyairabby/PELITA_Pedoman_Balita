package com.dicoding.pelita.profil

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelita.R
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextAlamat: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize your TextInputEditTexts
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextAlamat = findViewById(R.id.editTextAlamat)

        // Find the "Simpan" button
        val btnSave: Button = findViewById(R.id.btnSave)

        // Set a click listener for the button
        btnSave.setOnClickListener {
            // Get the edited data from the TextInputEditTexts
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val telepon = editTextPassword.text.toString()
            val alamat = editTextAlamat.text.toString()

            // TODO: Save the edited data or perform any desired actions

            // Finish the activity and navigate back to the ProfileFragment
            finish()
        }

        // showing the back button in the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // this event will enable the back
    // function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
