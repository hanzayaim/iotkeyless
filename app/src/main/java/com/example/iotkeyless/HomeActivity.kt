package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.iotkeyless.databinding.ActivityHomeBinding
import com.example.iotkeyless.model.FingerprintData
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

        binding.setVehicleNameBtn.setText("FreeGo 125")
    }

    private fun fetchDataFromFirebase() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (fingerprintSnapshot in dataSnapshot.children) {
                    val fingerprintData = fingerprintSnapshot.getValue(FingerprintData::class.java)

                    fingerprintData?.let {
                        val fingerpritnCheck = it.fingerprintCheck ?: false
                        val fingerprintId = it.fingerprintId ?: 0
                        val fingerprintStatus = it.fingerprintStatus ?: false

                        binding.keyIdContentTv.text = fingerprintId.toString()

                        if (!fingerprintStatus) {
                            binding.keyStatusContentTv.text = getString(R.string.off_status)
                            binding.setVehicleStatusBtn.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, R.color.green))
                            binding.setVehicleStatusBtn.setText(R.string.turn_on)

                            binding.setVehicleStatusBtn.setOnClickListener {
                                updateVehicleStatus(fingerprintSnapshot.ref, true)
                            }
                        } else {
                            binding.keyStatusContentTv.text = getString(R.string.on_status)
                            binding.setVehicleStatusBtn.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, R.color.red))
                            binding.setVehicleStatusBtn.setText(R.string.turn_off)

                            binding.setVehicleStatusBtn.setOnClickListener {
                                updateVehicleStatus(fingerprintSnapshot.ref, false)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read the fingerprint.", databaseError.toException())
            }
        })
    }

    private fun updateVehicleStatus(fingerprintRef: DatabaseReference, newStatus: Boolean) {
        fingerprintRef.child("fingerprintStatus").setValue(newStatus)
            .addOnCompleteListener {
                val message = if (newStatus) R.string.vehicle_turned_on else R.string.vehicle_turned_off
                Toast.makeText(this@HomeActivity, getString(message), Toast.LENGTH_SHORT).show()

                updateUI(newStatus)
            }
            .addOnFailureListener {
                Toast.makeText(this@HomeActivity, R.string.status_update_failed, Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(newStatus: Boolean) {
        val colorRes = if (newStatus) R.color.red else R.color.green
        val buttonTextRes = if (newStatus) R.string.turn_off else R.string.turn_on

        binding.setVehicleStatusBtn.setBackgroundColor(ContextCompat.getColor(this@HomeActivity, colorRes))
        binding.setVehicleStatusBtn.setText(getString(buttonTextRes))
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