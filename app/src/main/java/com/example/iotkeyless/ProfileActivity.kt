package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotkeyless.databinding.ActivityHomeBinding
import com.example.iotkeyless.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        showEmailPassword()
        isUserLogout()
        navbarNavigateTo()
        botbarNavigateTo()
    }

    private fun showEmailPassword() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val email = currentUser.email

            if (email != null) {
                binding.EmailProfileEt.setText(email)
                binding.PasswordProfileEt.setText("***************")
            } else {
                binding.EmailProfileEt.setText("NULL")
                binding.PasswordProfileEt.setText("NULL")
            }
        } else {
            binding.EmailProfileEt.setText("NULL")
            binding.PasswordProfileEt.setText("NULL")
        }
    }

    private fun isUserLogout() {
        binding.LogoutProfileBtn.setOnClickListener {
            firebaseAuth.signOut()
            navigateTo(LoginActivity::class.java)
            finish()
        }
    }

    private fun navigateTo(destination: Class<*>) {
        var intent = Intent(this, destination)
        startActivity(intent)
    }

    private fun navbarNavigateTo() {
        binding.hamburgerIv.setOnClickListener {
            navigateTo(MapsActivity::class.java)
        }
    }

    private fun botbarNavigateTo() {
        binding.homeBtn.setOnClickListener {
            navigateTo(HomeActivity::class.java)
        }

        binding.mapsBtn.setOnClickListener {
            navigateTo(MapsActivity::class.java)
        }

        binding.settingBtn.setOnClickListener {
            navigateTo(ProfileActivity::class.java)
        }
    }
}