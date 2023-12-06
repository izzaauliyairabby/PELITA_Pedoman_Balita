package com.dicoding.pelita.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.pelita.MainActivity
import com.dicoding.pelita.R
import com.dicoding.pelita.databinding.ActivityFragmentRegisBinding
import com.dicoding.pelita.register.RegisPageAdapter

class FragmenLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFragmentRegisBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var layoutIndicator: LinearLayout
    private val indicators = mutableListOf<View>()
    private lateinit var onboardingAdapter: RegisPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        btnNext = binding.btnNext
        layoutIndicator = binding.layoutIndicator

        val fragments = listOf(FragmentLogin()) // Use FragmentLogin instead of FragmentLogin
        onboardingAdapter = RegisPageAdapter(this, fragments)
        viewPager.adapter = onboardingAdapter

        // Inisialisasi indicator
        initIndicator()

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateButtonsAndIndicator(position)
            }
        })

        btnNext.setOnClickListener {
            // If there's only one page, you may not need this logic
            // Keep it if you plan to extend it later
            val currentItem = viewPager.currentItem
            if (currentItem + 1 < onboardingAdapter.itemCount) {
                viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        // Inisialisasi tombol dan indicator berdasarkan halaman awal
        updateButtonsAndIndicator(viewPager.currentItem)
    }

    private fun initIndicator() {
        val indicatorSize = viewPager.adapter?.itemCount ?: 0
        val indicatorWidth = resources.getDimensionPixelSize(R.dimen.indicator_width)
        val indicatorHeight = resources.getDimensionPixelSize(R.dimen.indicator_height)

        val layoutParams = LinearLayout.LayoutParams(indicatorWidth, indicatorHeight)
        layoutParams.setMargins(4, 0, 4, 0)

        for (i in 0 until indicatorSize) {
            indicators.add(View(this))
            indicators[i].apply {
                setBackgroundResource(R.drawable.indicator_notactive)
                this.layoutParams = layoutParams
            }
            layoutIndicator.addView(indicators[i])
        }

        updateIndicator(0)
    }

    private fun updateButtonsAndIndicator(position: Int) {
        // Mengatur warna teks dan latar belakang tombol
        if (position == 1) { // Jika di halaman kedua
            binding.relativelayout.setBackgroundColor((ContextCompat.getColor(this, R.color.colorPrimary)))
            binding.btnNext.text = "Mulai"
            binding.btnNext.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            binding.btnNext.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white))
        } else {
            binding.relativelayout.setBackgroundColor((ContextCompat.getColor(this, R.color.white)))
            binding.btnNext.text = "Selanjutnya"
            binding.btnNext.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            binding.btnNext.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }

        // Update indicator
        updateIndicator(position)
    }

    private fun updateIndicator(position: Int) {
        val indicatorSize = viewPager.adapter?.itemCount ?: 0
        for (i in 0 until indicatorSize) {
            indicators[i].setBackgroundResource(
                if (i == position) R.drawable.indicator_active else R.drawable.indicator_notactive
            )
        }
    }

    private fun finishOnboarding() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
