package gunawan.hendri.monitoringjarak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import gunawan.hendri.monitoringjarak.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRead.setOnClickListener { readData() }
        databaseListener()
    }

    private fun databaseListener() {
        database = FirebaseDatabase.getInstance().getReference("SENSOR")
        val postListener = object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val jarak = snapshot.child("JARAK").value
                binding.jarak.setText(jarak.toString() + " cm")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"FAILED", Toast.LENGTH_SHORT).show()
            }
        }
        database.addValueEventListener(postListener)
    }

    private fun readData() {
        database = FirebaseDatabase.getInstance().getReference("SENSOR")
        database.child("JARAK").get().addOnSuccessListener {
            if(it.exists()){
                val jarak:Float = it.value.toString().toFloat()
                Toast.makeText(this,"Successfull Read Data", Toast.LENGTH_SHORT).show()
                binding.jarak.setText(jarak.toString() + " cm")
            }else{
                Toast.makeText(this,"Path Does not Exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"FAILED", Toast.LENGTH_SHORT).show()
        }
    }
}