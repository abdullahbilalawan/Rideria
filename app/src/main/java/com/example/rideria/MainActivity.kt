
package com.example.rideria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MainActivity : AppCompatActivity() {
     var usertypeswitch: Switch? = null
     val name = UUID.randomUUID().toString()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        usertypeswitch = findViewById(R.id.switch1)
    }


    fun goClicked(view: View){

        if (usertypeswitch?.isChecked!!){

            FirebaseDatabase.getInstance().getReference().child("Users").child("driver"+name).child("id").setValue(name)


            val intent  = Intent(applicationContext,InfoActivity::class.java)

            intent.putExtra("id","driver"+name)
            intent.putExtra("type","driver")
            startActivity(intent)




        }

        else if(!(usertypeswitch?.isChecked!!)){


            FirebaseDatabase.getInstance().getReference().child("Users").child("Rider"+name).child("id").setValue(name)


            val intent  = Intent(applicationContext,InfoActivity::class.java)

            intent.putExtra("id","Rider"+name)
            intent.putExtra("type","Rider")
            startActivity(intent)




        }

    }

}