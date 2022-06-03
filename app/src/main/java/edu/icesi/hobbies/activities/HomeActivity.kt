package edu.icesi.hobbies.activities

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    //Fragments
    private lateinit var homeFragment: HomeFragment
    private lateinit var mapFragment: MapFragment
    private lateinit var profileFragment: ProfileFragment

    //Binding
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Instance fragments
        profileFragment = ProfileFragment.newInstance()
        mapFragment = MapFragment.newInstance()
        homeFragment = HomeFragment.newInstance()

        //Add Listeners

        //Subscriptions

        //-----------------------------------------------   NAVIGATION BAR   ---------------------------------------------
        binding.navigator.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeItem -> showFragment(homeFragment)
                R.id.mapItem -> showFragment(homeFragment)
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