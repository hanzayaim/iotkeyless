package com.example.iotkeyless

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.iotkeyless.databinding.ActivityMapsBinding
import com.example.iotkeyless.model.LocationData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapView: MapView
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance("https://iotkeyless-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Location")

        mapView = findViewById(R.id.maps)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }

        navbarNavigateTo()
        botbarNavigateTo()
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
        val defaultMarkerOptions = MarkerOptions().position(defaultMarkerLocation)
            .title("Default Vehicle")
            .snippet("My vehicle is here!")
        val defaultMarker = googleMap.addMarker(defaultMarkerOptions)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultMarkerLocation, 12f))

        googleMap.setOnMarkerClickListener { clickedMarker ->
            onMarkerClick(clickedMarker)
        }

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                googleMap.clear()

                for (locationSnapshot in dataSnapshot.children) {
                    val locationData = locationSnapshot.getValue(LocationData::class.java)
                    locationData?.let {
                        val lat = it.lat ?: 0.0
                        val long = it.long ?: 0.0
                        val location = LatLng(lat, long)

                        binding.latContentTv.text = lat.toString()
                        binding.longContentTv.text = long.toString()

                        val markerOptions = MarkerOptions().position(location)
                            .title("My Vehicle")
                            .snippet("My vehicle is here!")
                        googleMap.addMarker(markerOptions)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Failed to read the location", databaseError.toException())
            }
        })
    }

    private fun onMarkerClick(clickedMarker: Marker): Boolean {
        val clickedLat = clickedMarker.position.latitude
        val clickedLong = clickedMarker.position.longitude

        val geoUri = Uri.parse("geo:$clickedLat,$clickedLong?q=$clickedLat,$clickedLong(My Vehicle)")

        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(this@MapsActivity, "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    companion object {
        private const val GOOGLE_MAPS_URL = "http://maps.google.com/maps?daddr="
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