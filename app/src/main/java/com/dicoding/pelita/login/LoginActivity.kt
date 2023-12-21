package com.dicoding.pelita.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelita.MainActivity
import com.dicoding.pelita.R
import com.dicoding.pelita.databinding.ActivityLoginBinding
import com.dicoding.pelita.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firestore: FirebaseFirestore

    companion object {
        private const val RC_SIGN_IN = 9001
        const val EXTRA_NAMA = "NAMA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi GoogleSignInOptions
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // Inisialisasi GoogleSignInClient
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val password = binding.etPasswordLogin.text.toString()

            // Validasi email
            if (email.isEmpty()) {
                binding.etEmailLogin.error = "Email Harus Diisi"
                binding.etEmailLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi email tidak sesuai
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etPasswordLogin.error = "Email Tidak Valid"
                binding.etPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi password
            if (password.isEmpty()) {
                binding.etPasswordLogin.error = "Password Harus Diisi"
                binding.etPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            // Panggil fungsi loginFirebase dengan menyertakan nama
            loginFirebase(email, password)
        }

        // Tambahkan OnClickListener untuk tombol login dengan Google
        binding.btnSignInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    userId?.let {
                        firestore.collection("users").document(userId).get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val nama = documentSnapshot.getString("nama")
                                    Toast.makeText(this@LoginActivity, "Selamat datang $nama", Toast.LENGTH_SHORT).show()
                                    // Lanjutkan ke aktivitas utama atau lakukan tindakan lainnya
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle jika terjadi kesalahan
                                Toast.makeText(this@LoginActivity, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken

            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            Toast.makeText(this, "Selamat Datang: ${user?.displayName}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Login dengan Google gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
