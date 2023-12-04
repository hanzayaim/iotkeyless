package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.iotkeyless.databinding.ActivityHomeBinding
import com.example.iotkeyless.model.FingerprintData
import com.example.iotkeyless.model.LocationData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Fingerprint")

        showGreeting()
        fetchDataFromFirebase()
        navbarNavigateTo()
        mapsIvNavigateTo()
        botbarNavigateTo()
    }

    private fun showGreeting() {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val email = currentUser.email
            if (email != null) {
                binding.homeGreetingTv.setText("Welcome, $email")
            } else {
                binding.homeGreetingTv.setText("Welcome, Guest")
            }
        } else {
            binding.homeGreetingTv.setText("Welcome, Guest")
        }
    }

    private fun fetchDataFromFirebase() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (fingerprintSnapshot in dataSnapshot.children) {
                    val fingerprintData = fingerprintSnapshot.getValue(FingerprintData::class.java)

                    fingerprintData?.let {
                        val fingerprintCheck = it.fingerprintCheck ?: false
                        val fingerprintId = it.fingerprintId ?: 0
                        val fingerprintStatus = it.fingerprintStatus ?: false

                        binding.keyIdContentTv.text = fingerprintId.toString()
                        binding.keyStatusContentTv.text = fingerprintStatus.toString()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read the fingerprint.", databaseError.toException())
            }
        })
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

    private fun mapsIvNavigateTo() {
        binding.mapsIv.setOnClickListener {
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