package com.dicoding.pelita.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.pelita.databinding.ActivityRegisterBinding
import com.dicoding.pelita.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegis.setOnClickListener {
            // Launch FragmentRegisActivity when the button is clicked
            val intent = Intent(this, FragmentRegisActivity::class.java)
            startActivity(intent)
        }

        binding.textViewClickable.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
