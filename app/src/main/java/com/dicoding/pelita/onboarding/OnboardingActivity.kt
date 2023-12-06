package com.dicoding.pelita.onboarding

import OnboardingFragment2
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.pelita.MainActivity
import com.dicoding.pelita.R
import com.dicoding.pelita.databinding.ActivityOnboardingBinding
import com.dicoding.pelita.register.RegisterActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var layoutIndicator: LinearLayout
    private val indicators = mutableListOf<View>()
    private lateinit var onboardingAdapter: OnboardingPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        btnPrev = binding.btnPrev
        btnNext = binding.btnNext
        layoutIndicator = binding.layoutIndicator

        val fragments = listOf(OnboardingFragment1(), OnboardingFragment2())
        onboardingAdapter = OnboardingPagerAdapter(this, fragments)
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
            val currentItem = viewPager.currentItem
            if (currentItem + 1 < onboardingAdapter.itemCount) {
                viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        btnPrev.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
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
        // Update visibility tombol Prev
        btnPrev.visibility = if (position > 0) View.VISIBLE else View.GONE

        // Update teks tombol Next
        btnNext.text = if (position == onboardingAdapter.itemCount - 1) "Mulai" else "Selanjutnya"

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
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
