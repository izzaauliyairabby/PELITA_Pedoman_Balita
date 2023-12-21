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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonasiFragment : Fragment() {

    private lateinit var binding: FragmentDonasiBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

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

        // Mendapatkan informasi pengguna saat ini
        val currentUser = auth.currentUser

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser?.uid ?: "")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Ambil data nama dari snapshot
                val nama = snapshot.child("nama").value.toString()

                // Tampilkan nama di UI
                binding.tvUsername.text = nama
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

}
