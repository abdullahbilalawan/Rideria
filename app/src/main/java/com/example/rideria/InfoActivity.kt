package com.example.rideria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class InfoActivity : AppCompatActivity() {

    var nametext: EditText? = null

    var phoneno: EditText? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        nametext = findViewById(R.id.name)
        phoneno = findViewById(R.id.phone)






    }


    fun arrowClicked(view: View){

        var id = intent.getStringExtra("id")

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("Name").setValue(
            nametext?.text.toString())


        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("PhoneNo").setValue(
            phoneno?.text.toString())


        Toast.makeText(applicationContext,"Info Saved",Toast.LENGTH_LONG).show()



        if(intent.getStringExtra("type").equals("Rider")){

            val intent = Intent(applicationContext,RiderActivity::class.java)

            startActivity(intent)

        }

    }




}