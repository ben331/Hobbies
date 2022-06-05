package edu.icesi.hobbies.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {

    private lateinit var mapsFragment: MapsFragment

    //Binding
    private var _binding: ActivityLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding---------------------------------------------------------------
        _binding = ActivityLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mapsFragment = MapsFragment(true)

        //Show Map Fragment---------------------------------------------------------------
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mapsFragment)
        transaction.commit()

        //Cancel Button---------------------------------------------------------------
        binding.cancelBtn.setOnClickListener{
            val intent = Intent(this, EventFormActivity::class.java)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }

        //Select Location Button---------------------------------------------------------------
        binding.selectBtn.setOnClickListener{
            val pos = mapsFragment.eventMarker?.position
            if(pos!=null){
                val intent = Intent(this, EventFormActivity::class.java).apply {
                    putExtra("type", type)
                    putExtra("lat", pos.latitude)
                    putExtra("lon", pos.longitude)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                val alert = AlertDialog.Builder(this)
                alert.setMessage("Location not selected")
                alert.create()
                alert.apply {
                    setNeutralButton("Ok"){dialog,_->dialog.dismiss()}
                }
                alert.show()
            }
        }

        //Instructions---------------------------------------------------------------
        Toast.makeText(this, "Touch to select location", Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(msg:String){

    }
}