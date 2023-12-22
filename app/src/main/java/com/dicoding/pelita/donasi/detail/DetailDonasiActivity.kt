package com.dicoding.pelita.donasi.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.dicoding.pelita.databinding.ActivityDetailDonasiBinding
import com.dicoding.pelita.models.DonationItem
import android.graphics.Color
import android.graphics.PorterDuff
import android.content.res.Configuration


class DetailDonasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDonasiBinding
    private lateinit var donationItem: DonationItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDonasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Toolbar
        val toolbar: Toolbar = binding.toolbarDetailDonasi
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        val color = if (isDarkTheme) Color.WHITE else Color.BLACK
        toolbar.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)


        // Mendapatkan data donasi dari intent
        donationItem = intent.getParcelableExtra(EXTRA_DONATION_ITEM)!!

        // Set listener untuk tombol donasi
        binding.btnDonasi.setOnClickListener {
            // Handle klik tombol donasi di sini
            Toast.makeText(this, "Button Donasi Clicked", Toast.LENGTH_SHORT).show()
        }

        // Tampilkan detail donasi pada tampilan
        displayDonationDetails(donationItem)
    }

    private fun displayDonationDetails(donationItem: DonationItem) {
        // Implementasikan logika untuk menampilkan detail pada tampilan
        // ...

        // Contoh: mengganti teks pada TextView
        binding.tvTitleDetailDonasi.text = donationItem.title
        binding.tvOrganizerDetailDonasi.text = donationItem.organizer
        binding.tvContactDetailDonasi.text = donationItem.contact
        binding.tvDescription.text = donationItem.description
        binding.tvDataParticipant.text = donationItem.participants
        binding.tvDataTotalDonation.text = donationItem.totalDonation

        // Menggunakan Glide untuk menampilkan gambar
        Glide.with(this)
            .load(donationItem.imageUrl)
            .centerCrop()
            .into(binding.ivDetail)
    }

    // Override untuk memberikan fungsi kembali ke aktivitas sebelumnya
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_DONATION_ITEM = "extra_donation_item"
    }
}
