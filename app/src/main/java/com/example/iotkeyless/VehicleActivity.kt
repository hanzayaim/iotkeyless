package com.example.iotkeyless

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.iotkeyless.databinding.ActivityVehicleBinding
import com.example.iotkeyless.model.LocationData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VehicleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleBinding
    private lateinit var mapView: MapView
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance("https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Location")

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
        googleMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
            isScrollGesturesEnabled = true
            isZoomGesturesEnabled = true
            isRotateGesturesEnabled = true
            isTiltGesturesEnabled = true
        }

        val defaultMarkerLocation = LatLng(-6.201935, 106.781525)

        val defaultMarkerOptions = MarkerOptions().position(defaultMarkerLocation).title("Default Vehicle").snippet("My vehicle is here!")
        val defaultMarker = googleMap.addMarker(defaultMarkerOptions)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMarkerLocation, 12f))

        val defaultLat = defaultMarker!!.position.latitude
        val defaultLong = defaultMarker!!.position.longitude

        googleMap.setOnMarkerClickListener { clickedMarker ->
            if (clickedMarker.position == defaultMarker.position) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=$defaultLat,$defaultLong"))
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
                startActivity(intent)
                true
            }
            else {
                false
            }
        }

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                googleMap.clear()

                for (locationSnapshoot in dataSnapshot.children) {
                    val locationData = locationSnapshoot.getValue(LocationData::class.java)
                    locationData?.let {


                        val lat = it.lat ?: 0.0
                        val long = it.long ?: 0.0
                        val location = LatLng(lat, long)

                        val markerOptions = MarkerOptions().position(location).title("My Vehicle").snippet("My vehicle is here!")
                        googleMap.addMarker(markerOptions)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read the location", databaseError.toException())
            }
        })

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