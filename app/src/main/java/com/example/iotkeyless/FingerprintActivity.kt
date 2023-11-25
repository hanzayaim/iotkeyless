package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotkeyless.databinding.ActivityFingerprintBinding
import com.google.firebase.auth.FirebaseAuth

class FingerprintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFingerprintBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerprintBinding.inflate(layoutInflater)
        setContentView(binding.root)

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