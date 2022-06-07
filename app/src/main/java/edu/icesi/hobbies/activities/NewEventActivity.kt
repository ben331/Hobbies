package edu.icesi.hobbies.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.databinding.ActivityNewEventBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.Event
import java.time.LocalDate

class NewEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewEventBinding

    private var db = Firebase.firestore

    private var pos:LatLng?=null
    private lateinit var date:LocalDate

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

            //Read data
            val name = binding.editTextEventTitle.text.toString()
            val description:String= binding.editTextEventDescription.text.toString()
            val day = binding.editTextDay.text.toString()
            val month = binding.editTextMonth.text.toString()
            val year = binding.editTextYear.text.toString()
            val participants = binding.editTextEventNumber.text.toString()

            //Verify formats--------------------------------------------------
            val warningMessage = if(
                name=="" &&
                day=="" && month=="" && year=="" &&
                participants==""
            ){
                "Empty fields"
            }else{
                if(pos==null){
                    "Location not selected"
                }else{
                    if(name.length<5){
                        "Title too short"
                    }else{
                        if(participants.toInt()<2){
                            "Very few participants"
                        }else{
                            isCorrectDate(day.toInt(), month.toInt(), year.toInt())
                        }
                    }
                }
            }
            //--------------------------------------------------
            if (warningMessage!="Correct"){
                val builder = AlertDialog.Builder(this)
                builder.setMessage(warningMessage)
                builder.create()
                builder.show()
            }else{

                    //Get current club
                val chatClubId = intent.extras?.get("clubId").toString()
                var club:Club?
                db.collection("clubs").document(chatClubId).get().addOnSuccessListener {
                    club = it.toObject(Club::class.java)

                    //ID = CLUB_ID + #Event
                    val id="${club?.id}_${club?.totalEvents?.plus(1)}"

                    //Create Event--------------------------------------------------------------------------------------------------
                    val event = Event(
                        id,
                        name,
                        club?.name!!,
                        "- - -",
                        club?.imageUri.toString(),
                        chatClubId,
                        pos?.latitude!!,
                        pos?.longitude!!,
                        description,
                        date.dayOfMonth,
                        date.monthValue,
                        date.year,
                        participants.toInt()
                    )
                    uploadEvent(event)
                }
            }
        }
    }

    private fun uploadEvent(event:Event){
        db.collection("events").document(event.id).set(event).addOnSuccessListener {
            val intent = Intent(this, ChatActivity::class.java).apply {
                putExtra("chatId", event.chatClubId)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,"Fail to upload event",Toast.LENGTH_LONG).show()
        }
    }

    private fun onResultLocationSelected(result:ActivityResult){
        if(result.resultCode== RESULT_OK){
            val lat = result.data?.extras?.getDouble("lat", 0.506)!!
            val lon = result.data?.extras?.getDouble("lon", 0.723)!!
            pos = LatLng(lat, lon)
            binding.editTextEventPlace.setText(pos.toString())
        }else{
            Toast.makeText(this, "Location not selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isCorrectDate(day:Int, month:Int, year:Int): String{
        val message: String

        //Verify non negatives and month
        if((month in 1..12) && day>0 && (year in 2022..2100)){

            //Verify numbers of days
            val isCorrect = if(month==1 || month==3 || month==5 || month==7
                || month==8 || month==10 || month==12){
                (day in 1..31)
            }else if(month==4 || month==6 || month==9 || month==11){
                (day in 1..30)
            }else{
                if (year%4==0){
                    (day in 1..29)
                }else{
                    (day in 1..28)
                }
            }

            message = if(isCorrect){
                //Verify if its After now
                date = LocalDate.of(year,month,day)
                if (date.isAfter(LocalDate.now())){
                    "Correct"
                }else{
                    "Date must be in the future"
                }
            }else{
                "Day out of range"
            }

        }else{
            message="Please type positive numbers in calendar range"
        }
        return message
    }
}