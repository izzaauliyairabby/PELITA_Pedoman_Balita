package com.dicoding.pelita.donasi

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.pelita.databinding.FragmentDonasiBinding
import com.dicoding.pelita.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DonasiFragment : Fragment() {

    private lateinit var binding: FragmentDonasiBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonasiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Mendapatkan informasi pengguna saat ini
        val currentUser = auth.currentUser

        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    // Ambil data nama dari snapshot
                    val nama = documentSnapshot.getString("nama")

                    // Tampilkan nama di UI
                    binding.tvUsername.text = nama
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }
}
