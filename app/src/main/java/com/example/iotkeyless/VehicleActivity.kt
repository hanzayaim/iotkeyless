package com.example.iotkeyless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.iotkeyless.databinding.ActivityVehicleBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference

class VehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleBinding
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = findViewById(R.id.maps)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }

        topBarNavigate()
        tabMenuNavigate()
        bottomBarNavigate()
    }

    private fun onMapReady(googleMap: GoogleMap) {
        val latitude = -6.201935
        val longtitude = 106.781525

        val location = com.google.android.gms.maps.model.LatLng(latitude, longtitude)

        val markerOptions = MarkerOptions().position(location).title("My Location").snippet("Location Description")

        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location))

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