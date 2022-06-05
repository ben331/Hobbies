package edu.icesi.hobbies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.icesi.hobbies.databinding.ActivityNewClubBinding
import edu.icesi.hobbies.databinding.ActivityNewEventBinding
import edu.icesi.hobbies.databinding.FragmentNewEventBinding
import edu.icesi.hobbies.model.Event
import java.time.LocalDate
import java.util.*

class NewEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewEventBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnEvenReturn.setOnClickListener {
            finish()
        }

        binding.btnEventSend.setOnClickListener {
            try {
                val dates=binding.editTextEventDate.text.toString().split('/')
                val day = dates[0].toInt()
                val month = dates[1].toInt()
                val year = dates[2].toInt()


                val id:String= UUID.randomUUID().toString()
                val name = binding.editTextEventTitle.text.toString()


                val clubName = intent.extras?.get("clubName").toString()
                val placeName = intent.extras?.get("placeName").toString()
                val imgClubUri = intent.extras?.get("imgClubUri").toString()
                val chatClubId = intent.extras?.get("chatClubId").toString()

                val lat = 0.506
                val lng = 0.723

                //
                val description:String= binding.editTextEventDescription.text.toString()
                val date = LocalDate.of(year,month,day)
                val participants:Int= binding.editTextEventNumber.text.toString().toInt()

                val even = Event(id,name,clubName,placeName,imgClubUri,chatClubId,lat,lng,description,date,participants)
                
            }catch (e:Exception){
                Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
            }

        }
    }
}