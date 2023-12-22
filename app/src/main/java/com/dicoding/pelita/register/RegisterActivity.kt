package com.dicoding.pelita.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelita.databinding.ActivityRegisterBinding
import com.dicoding.pelita.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegis.setOnClickListener {
            val email = binding.etEmailRegis.text.toString()
            val password = binding.etPasswordRegis.text.toString()

            if (email.isEmpty()) {
                binding.etEmailRegis.error = "Email Harus Diisi"
                binding.etEmailRegis.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmailRegis.error = "Email Tidak Valid"
                binding.etEmailRegis.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPasswordRegis.error = "Password Harus Diisi"
                binding.etPasswordRegis.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 8) {
                binding.etPasswordRegis.error = "Password Minimal 8 Karakter"
                binding.etPasswordRegis.requestFocus()
                return@setOnClickListener
            }

            val nama = binding.etNamaRegis.text.toString()

            registerWithEmailCustom(email, password, nama)
        }
    }

    private fun registerWithEmailCustom(email: String, password: String, nama: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser

                    // Simpan data ke Cloud Firestore
                    val userId = currentUser?.uid
                    userId?.let {
                        val userMap = hashMapOf(
                            "email" to email,
                            "nama" to nama
                        )
                        firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d("FireStore", "User data saved succesfully")
                        }
                            .addOnFailureListener { e->
                                Log.w("FireStore", "Eror saving user data", e)
                            }
                    }

                    Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Gagal mendaftar: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
