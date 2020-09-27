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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ViewRequestsActivity : AppCompatActivity() {

    var requestslistviw: ListView? = null
    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null

    var name: String? =null


    var requests: ArrayList<String> = ArrayList()


    var nearbylats: ArrayList<Double> = ArrayList()

    var nearbylongs: ArrayList<Double> = ArrayList()
    var mintent: Intent? =null


    fun updateListView(location: Location) {







        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, requests)
        requestslistviw?.adapter = adapter




        val databaseRef = FirebaseDatabase.getInstance().getReference().child("Users")


        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                name = dataSnapshot.child("Name").value.toString()



                if(dataSnapshot.child("latitude").value!= null) {
                    mintent?.putExtra("RiderName",name)
                    val latitude_value = dataSnapshot.child("latitude").value.toString().toDouble()
                    val longitude_value =
                        dataSnapshot.child("longitude").value.toString().toDouble()



                    nearbylats.add(latitude_value)

                    nearbylongs.add(longitude_value)
                          val nearbyloc = LatLng(latitude_value, longitude_value)


                var distance = location.distanceTo(Location(LocationManager.GPS_PROVIDER).apply {
                    latitude = latitude_value
                    longitude = longitude_value



                })





                    requests.clear()


                   requests.add(name + "  distance:  " + distance.toInt())

                   adapter.notifyDataSetChanged()





            }}

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }



        databaseRef.addChildEventListener(childEventListener)






    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_requests)


        setTitle("NearBy Requests")

        mintent = Intent(applicationContext,DriverLocationActivity::class.java)

        requestslistviw = findViewById(R.id.list)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, requests)
        requestslistviw?.adapter = adapter


        requestslistviw?.onItemClickListener =  AdapterView.OnItemClickListener { adapterView, view, i, l ->


            val lastKnownLocation =
                locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER);


            if(lastKnownLocation!= null){




                mintent?.putExtra("requestLatitude",nearbylats.get(i))
                mintent?.putExtra("requestLongitude",nearbylongs.get(i))
                mintent?.putExtra("driverLatitude", lastKnownLocation.latitude)
                mintent?.putExtra("driverLongitude",lastKnownLocation.longitude)

                var id = intent.getStringExtra("id")
                mintent?.putExtra("id",id)












                startActivity(mintent)





            }






        }





        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {

                var id = intent.getStringExtra("id")

                if (p0 != null) {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("driverlocation").child("latitude").setValue(p0.latitude)
                    FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("driverlocation").child("longitude").setValue(p0.longitude)

                }

                if (p0 != null) {
                    updateListView(p0)
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

                    updateListView(lastKnownLocation)



                }


            }








        }
}}