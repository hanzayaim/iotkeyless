package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.iotkeyless.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var isConnectedToDevice = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        showGreetings()
        topBarNavigate()
        tabMenuNavigate()
        bottomBarNavigate()

        binding.ivBtnConnectToDevice.setOnClickListener {
            isConnectToDevice()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showGreetings() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                binding.tvWelcomeUser.setText("Welcome, $email")
            }
            else {
                binding.tvWelcomeUser.setText("Welcome, Anonymous")
            }
        }
        else {
            binding.tvWelcomeUser.setText("Welcome, Anonymous")
        }
    }

    private fun navigateTo(destination: Class<*>) {
        var intent = Intent(this, destination)
        startActivity(intent)
    }

    private fun topBarNavigate() {
        binding.ivProfile.setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }
    }

    private fun tabMenuNavigate() {
        binding.ivBtnDevice.setOnClickListener {
            navigateTo(MainActivity::class.java)
        }

        binding.ivBtnFingerprint.setOnClickListener {
             navigateTo(FingerprintActivity::class.java)
        }

        binding.ivBtnVehicle.setOnClickListener {
            navigateTo(VehicleActivity::class.java)
        }
    }

    private fun isConnectToDevice() {
        isConnectedToDevice = !isConnectedToDevice

        if (isConnectedToDevice) {
            binding.ivBtnConnectToDevice.setImageResource(R.drawable.ic_disconnected_from_device)
            showToast("Device connected!")
        }
        else {
            binding.ivBtnConnectToDevice.setImageResource(R.drawable.ic_connect_to_device)
            showToast("Device disconnected!")
        }
    }

    private fun bottomBarNavigate() {
        binding.ivBtnHome.setOnClickListener {
            navigateTo(MainActivity::class.java)
        }
    }
}