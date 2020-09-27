package com.example.rideria

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class DriverLocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap




    fun acceptreq(view: View){
        val name = intent.getStringExtra("RiderName")
        val ridername = intent.getStringExtra("id")
        var key:String = ""



        val query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Name").equalTo(
            name
        )
        val valueEventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    key = ds.key.toString()
                    FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Driver").setValue(
                        ridername
                    )

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        query.addListenerForSingleValueEvent(valueEventListener)


        // adding data

        val iintent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr="+intent.getDoubleExtra("driverLatitude",0.0)+","+intent.getDoubleExtra("driverLongitude",0.0)+"&daddr="+intent.getDoubleExtra("requestLatitude",0.0)+","+intent.getDoubleExtra("requestLongitude",0.0))
        )
        startActivity(iintent)



















       // FirebaseDatabase.getInstance().getReference().child("Users").child("Booked").child("Booked by: "+name).setValue(rider_id)






    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_location)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        var mapLayout: ConstraintLayout  = findViewById(R.id.layout)

        mapLayout.viewTreeObserver.addOnGlobalLayoutListener(object:
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // your code here. `this` should work


                var markers: ArrayList<Marker> = ArrayList()



                val request = LatLng(
                    intent.getDoubleExtra("requestLatitude", 0.0), intent.getDoubleExtra(
                        "requestLatitude",
                        0.0
                    )
                )
                val driver = LatLng(
                    intent.getDoubleExtra("driverLatitude", 0.0), intent.getDoubleExtra(
                        "driverLatitude",
                        0.0
                    )
                )

                markers.add(mMap.addMarker(MarkerOptions().position(request).title("Request Location")))

                markers.add(mMap.addMarker(MarkerOptions().position(driver).title("Drivers Location")))


                val builder = LatLngBounds.Builder()

                for ( marker: Marker in markers){

                    builder.include(marker.position)
                }

                val bounds = builder.build()

                val padding = 30

                val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

                mMap.animateCamera(cu)



                // Add a marker move the camera







            }
        })











        setTitle("Drivers Location and Map")
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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







    }
}