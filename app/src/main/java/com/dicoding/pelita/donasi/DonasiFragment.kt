package com.dicoding.pelita.donasi

import DonasiAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.pelita.databinding.FragmentDonasiBinding
import com.dicoding.pelita.donasi.detail.DetailDonasiActivity
import com.dicoding.pelita.models.DummyDataRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

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

        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser

        currentUser?.uid?.let { userId ->
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { documentSnapshot ->
                    val nama = documentSnapshot.getString("nama")
                    binding.tvUsername.text = nama
                }
                .addOnFailureListener {
                    // Handle error with a toast
                    Toast.makeText(
                        requireContext(),
                        "Failed to retrieve user data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        displayDummyDonations()
    }

    private fun displayDummyDonations() {
        val dummyDonationList = DummyDataRepository.dummyDonationList

        val adapter = DonasiAdapter(dummyDonationList)
        binding.rvList.adapter = adapter

        adapter.onDonasiButtonClickListener = { donationItem ->
            val intent = Intent(requireContext(), DetailDonasiActivity::class.java).apply {
                putExtra(DetailDonasiActivity.EXTRA_DONATION_ITEM, donationItem)
            }
            startActivity(intent)
        }
    }
}
