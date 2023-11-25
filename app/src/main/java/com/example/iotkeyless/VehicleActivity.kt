package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotkeyless.databinding.ActivityVehicleBinding
import com.google.firebase.auth.FirebaseAuth

class VehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
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