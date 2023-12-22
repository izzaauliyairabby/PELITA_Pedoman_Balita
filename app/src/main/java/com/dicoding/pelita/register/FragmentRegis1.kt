package com.dicoding.pelita.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.pelita.R
import com.dicoding.pelita.databinding.FragmentRegisBinding

class FragmentRegis1 : Fragment() {

    private var _binding: FragmentRegisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tampilkan tulisan di TextView atau elemen UI lainnya
        binding.imageViewRegis.setImageResource(R.drawable.regis1)
        binding.textViewTitle.text = "Berhasil Didaftarkan"
        binding.textViewDeskripsi.text =" PELITA adalah Tempat Pertumbuhan\n "+ "Bergizi Untuk Bayi Anda"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
