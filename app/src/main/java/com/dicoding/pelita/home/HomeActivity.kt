package com.dicoding.pelita.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.pelita.databinding.FragmentHomeBinding
import com.dicoding.pelita.home.makanan.DetailMakananActivity
import com.dicoding.pelita.home.resep.DetailResepActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
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

        binding.btnMakanan.setOnClickListener {
            // Create an Intent to start the DetailMakananActivity
            val intent = Intent(requireContext(), DetailMakananActivity::class.java)
            startActivity(intent)
        }

        binding.btnResep.setOnClickListener {
            // Create an Intent to start the DetailResepActivity
            val intent = Intent(requireContext(), DetailResepActivity::class.java)
            startActivity(intent)
        }
    }
}
