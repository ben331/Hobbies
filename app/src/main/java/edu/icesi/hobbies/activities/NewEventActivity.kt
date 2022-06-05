package edu.icesi.hobbies.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.databinding.ActivityNewClubBinding
import edu.icesi.hobbies.databinding.ActivityNewEventBinding
import edu.icesi.hobbies.databinding.FragmentNewEventBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.Event
import kotlinx.android.synthetic.main.fragment_live_map.*
import java.time.LocalDate
import java.util.*

class NewEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewEventBinding

    private var db = Firebase.firestore

    private var pos:LatLng = LatLng(0.506, 0.723)

    //Launchers
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onResultLocationSelected)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnEvenReturn.setOnClickListener {
            finish()
        }

        binding.btnPlaceSelecter.setOnClickListener{
            val intent = Intent(this, LocationActivity::class.java)
            launcher.launch(intent)
        }

        binding.btnEventSend.setOnClickListener {
            try {
                val dates=binding.editTextEventDate.text.toString().split('/')
                val day = dates[0].toInt()
                val month = dates[1].toInt()
                val year = dates[2].toInt()

                val id:String= UUID.randomUUID().toString()
                val name = binding.editTextEventTitle.text.toString()

                var club:Club?=null
                val chatClubId = intent.extras?.get("clubId").toString()
                db.collection("clubs").document(chatClubId).get().addOnSuccessListener {
                     club = it.toObject(Club::class.java)
                }

                val clubName = club?.name!!

                //
                val description:String= binding.editTextEventDescription.text.toString()
                val date = LocalDate.of(year,month,day)
                val participants:Int= binding.editTextEventNumber.text.toString().toInt()

                val event = Event(id,name,clubName,"placeName","imgClubUri",chatClubId,pos,description,date,participants)
                db.collection("events").document(id).set(event).addOnSuccessListener {
                    val intent = Intent(this, ChatActivity::class.java).apply {
                        putExtra("chatId", chatClubId)
                        putExtra("event", event)
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }.addOnFailureListener{

                }
                
            }catch (e:Exception){
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onResultLocationSelected(result:ActivityResult){
        val lat = result.data?.extras?.getDouble("lat", 0.506)!!
        val lon = result.data?.extras?.getDouble("lon", 0.723)!!
        pos = LatLng(lat, lon)
    }
}