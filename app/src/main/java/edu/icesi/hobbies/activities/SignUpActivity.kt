package edu.icesi.hobbies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.txtAlreadyHaveAccount.setOnClickListener{
            finish()
        }

        binding.btnSignupConfirm.setOnClickListener(::register)
    }

    private fun register(view: View) {
        Firebase.auth.createUserWithEmailAndPassword(
            binding.editTextSignupEmail.text.toString(),
            binding.editTextSignupPassword1.text.toString()
        ).addOnSuccessListener {
            val id = Firebase.auth.currentUser?.uid

        }
    }
}