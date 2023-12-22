package com.dicoding.pelita.home.makanan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.pelita.R

class DetailMakananActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_makanan)

        // Aktifkan tombol "back" di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Tangani tindakan saat tombol "back" ditekan
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}