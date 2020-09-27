package com.example.rideria

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_rider.*


class RiderActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null



    fun logout(view: View){

        val intent = Intent(applicationContext,MainActivity::class.java)

        startActivity(intent)




    }




    fun call(view: View){




        // when ride booking button is clicked
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.1f,
                locationListener
            )
            val lastKnownLocation =
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER);




            if (lastKnownLocation != null) {
                var id = intent.getStringExtra("id")

              val latLng = LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)

                FirebaseDatabase.getInstance().getReference().child("Users").child(id)
                    .child("latitude").setValue(latLng.latitude)

                FirebaseDatabase.getInstance().getReference().child("Users").child(id)
                    .child("longitude").setValue(latLng.longitude)
                Toast.makeText(applicationContext, "Calling ur Rider", Toast.LENGTH_LONG).show()
            } else {

                Toast.makeText(
                    applicationContext,
                    "COuld not find your location",
                    Toast.LENGTH_LONG
                ).show()

            }

            b.setText("CANCEL RIDE")
        }















    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        0f,
                        locationListener
                    )
                    val lastKnownLocation =
                        locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    updateMap(lastKnownLocation)
                }
            }
        }
    }

    fun updateMap(location: Location) {




        val userLocation = LatLng(location.latitude, location.longitude)
        mMap.clear()
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
        mMap.addMarker(MarkerOptions().position(userLocation).title("Your Location"))
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        setTitle("Riders Map and Location")



//        val button: Button = findViewById(R.id.b)
//        if(button.text.equals("CANCEL RIDE")){
//            var id = intent.getStringExtra("id")
//
//
//            FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("location").removeValue()
//
//            Toast.makeText(applicationContext,"Cancelling and removing request",Toast.LENGTH_LONG).show()
//        }



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.layout) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationListener = object : LocationListener{
            override fun onLocationChanged(p0: Location?) {

                if (p0 != null) {
                    updateMap(p0)
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {

            }

            override fun onProviderDisabled(p0: String?) {

            }

        }

        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0.1f,
                locationListener
            );

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                ActivityCompat.requestPermissions(this, permissions, 0)


            } else {

                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0.1f,
                    locationListener
                );

                val lastKnownLocation =
                    locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (lastKnownLocation != null) {

                    updateMap(lastKnownLocation)



                }


            }



    }
}}


