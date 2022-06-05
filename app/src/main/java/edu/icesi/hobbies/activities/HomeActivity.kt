package edu.icesi.hobbies.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.R
import edu.icesi.hobbies.adapters.ChatAdapter
import edu.icesi.hobbies.databinding.ActivityHomeBinding
import edu.icesi.hobbies.model.Chat
import edu.icesi.hobbies.model.User
import java.util.*

class HomeActivity : AppCompatActivity() {

    //Fragments
    private lateinit var homeFragment: HomeFragment
    private lateinit var liveMapFragment: LiveMapFragment
    private lateinit var profileFragment: ProfileFragment

    //Binding
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    //Vars
    private var haveLocationPermissions = false
    private var db = Firebase.firestore
    private var user = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Instance fragments
        profileFragment = ProfileFragment.newInstance()
        liveMapFragment = LiveMapFragment.newInstance()
        homeFragment = HomeFragment.newInstance()

        //Add Listeners

        //Subscriptions

        //-----------------------------------------------   NAVIGATION BAR   ---------------------------------------------
        binding.navigator.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeItem -> showFragment(homeFragment)
                R.id.mapItem -> if(haveLocationPermissions) showFragment(liveMapFragment) else requestLocationPermissions()
                R.id.profileItem -> showFragment(profileFragment)
            }
            true
        }
    }

    //-----------------------------------------------   FRAGMENTS   ---------------------------------------------
    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    //-----------------------------------------------   TOOL BAR   ----------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar, menu)
        return true
    }
    override fun onOptionsItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.search_action -> Toast.makeText(this, "Search Button Pressed", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(menu)
    }

    //-----------------------------------------------   PERMISSIONS   ---------------------------------------------
    private fun requestLocationPermissions(){
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }

    //Result after ask for permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        haveLocationPermissions = true
        for (result in grantResults) {
            haveLocationPermissions = haveLocationPermissions && (result!=-1)
        }

        if(haveLocationPermissions) showFragment(liveMapFragment)
    }

    //-----------------------------------------------   CLOSE APP   ---------------------------------------------
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if(keyCode== KeyEvent.KEYCODE_BACK){
            if(homeFragment.isAdded){
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.alert_exit)
                builder.apply {
                    setPositiveButton(R.string.yes) { _, _ ->
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.addCategory(Intent.CATEGORY_HOME)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                }
                builder.create()
                builder.show()
            }else{
                showFragment(homeFragment)
            }
        }
        return true
    }
}