package com.dicoding.pelita.profil

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dicoding.pelita.databinding.FragmentProfileBinding
import com.dicoding.pelita.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val EDIT_PROFILE_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Mendapatkan informasi pengguna saat ini
        val currentUser = auth.currentUser

        // Menampilkan informasi pengguna di UI
        binding.tvNama.text = currentUser?.displayName
        binding.tvEmail.text = currentUser?.email

        // Menambahkan OnClickListener untuk tombol logout
        binding.btnLogout.setOnClickListener {
            logout()
        }

        binding.btnEditProfil.setOnClickListener {
            // Start EditProfileActivity with startActivityForResult
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        fetchAndDisplayUserProfile()
    }

    // Handle the result from EditProfileActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Reload the profile data or update UI here
            // You might want to call a function that fetches the updated data and updates the UI
            // For example: fetchAndDisplayUserProfile()
            fetchAndDisplayUserProfile()
        }
    }

    private fun fetchAndDisplayUserProfile() {
        // Coba ambil URL gambar dari Firestore dan tampilkan di ImageView
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        userId?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot? ->
                    if (documentSnapshot != null) {
                        val imageUri = documentSnapshot.getString("imageUri")
                        if (imageUri != null && imageUri.isNotEmpty()) {
                            // Load gambar ke ImageView menggunakan Glide
                            Glide.with(requireContext())
                                .load(imageUri)
                                .into(binding.ivProfile)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle error
                    Toast.makeText(requireContext(), "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun logout() {
        auth.signOut()

        // Menampilkan toast saat berhasil logout
        Toast.makeText(requireContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show()
        // Kembali ke halaman login setelah logout
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
