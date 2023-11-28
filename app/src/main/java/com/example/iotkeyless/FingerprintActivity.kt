package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iotkeyless.adapter.FingerprintAdapter
import com.example.iotkeyless.databinding.ActivityFingerprintBinding
import com.example.iotkeyless.model.FingerprintData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FingerprintActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFingerprintBinding
    private lateinit var fingerprintList: ArrayList<FingerprintData>
    private lateinit var adapter: FingerprintAdapter
    var databaseReference: DatabaseReference? = null
    var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFingerprintBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@FingerprintActivity, 1)
        binding.rvFingerprint.layoutManager = gridLayoutManager

        fingerprintList = ArrayList()
        adapter = FingerprintAdapter(this@FingerprintActivity, fingerprintList)
        binding.rvFingerprint.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance("https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Fingerprints")

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                fingerprintList.clear()
                for (itemSnapshot in snapshot.children) {
                    val fingerprintData = itemSnapshot.getValue(FingerprintData::class.java)
                    if (fingerprintData != null) {
                        fingerprintList.add(fingerprintData)
                    }
                }
                adapter.notifyDataSetChanged()

                for (data in fingerprintList) {
                    Log.d("FingerprintAdapter", "ID: ${data.fingerprintId}, Name: ${data.fingerprintName}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        topBarNavigate()
        tabMenuNavigate()
        bottomBarNavigate()
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

    private fun bottomBarNavigate() {
        binding.ivBtnHome.setOnClickListener {
            navigateTo(MainActivity::class.java)
        }
    }
}