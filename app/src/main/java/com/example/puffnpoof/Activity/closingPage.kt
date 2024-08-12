package com.example.puffnpoof.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.puffnpoof.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class closingPage : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map:GoogleMap
    private lateinit var currLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closing_page)
        supportActionBar?.hide()

        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)

        getLocation()

        val button= findViewById<Button>(R.id.yes_button)

        button.setOnClickListener {
            val intent= Intent(this,LoginPage::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    fun getLocation(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1)
            return
        }
        var task: Task<Location> = fusedLocationProviderClient.getLastLocation()
        task.addOnSuccessListener { location: Location? ->
            if(location!= null){
                currLocation=location
            }else{
                currLocation=Location("").apply {
                    currLocation.longitude= 106.78113
                    currLocation.latitude= -6.20201
                }

            }
            var mapFragment:SupportMapFragment= getSupportFragmentManager().findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestcode: Int,
        permission: Array<out String>,
        grantResult:IntArray
    ){
        super.onRequestPermissionsResult(requestcode,permission,grantResult)
        if(requestcode==1){
            if(grantResult.size>0 && grantResult[0]== PackageManager.PERMISSION_GRANTED){
                getLocation()
            }else{
                Toast.makeText(this,"Location Permission Is Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap

        var myLocation= LatLng(currLocation.latitude, currLocation.longitude)
        var cameraPosition= CameraPosition.Builder().target(myLocation).zoom(18.0F).tilt(70.0F).build()
        map.addMarker(MarkerOptions().position(myLocation).title("PuFF and Poof"))
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        map.isBuildingsEnabled = true
    }
}