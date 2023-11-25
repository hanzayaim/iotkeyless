package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotkeyless.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val email = currentUser.email

            if (email != null) {
                val greetings = binding.tvWelcomeUser.setText("Welcome, $email")
            }
            else {
                val greetings = binding.tvWelcomeUser.setText("Welcome, Anonymous")
            }
        }
        else {
            val greetings = binding.tvWelcomeUser.setText("Welcome, Anonymous")
        }

        moveToPage()
    }

    private fun moveToPage() {
        binding.ivBtnDevice.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.ivBtnFingerprint.setOnClickListener {
            var intent = Intent(this, FingerprintActivity::class.java)
            startActivity(intent)
        }

        binding.ivBtnVehicle.setOnClickListener {
            var intent = Intent(this, VehicleActivity::class.java)
            startActivity(intent)
        }
    }
}