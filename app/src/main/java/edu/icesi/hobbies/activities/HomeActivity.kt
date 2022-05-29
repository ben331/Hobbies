package edu.icesi.hobbies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}