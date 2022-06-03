package edu.icesi.hobbies.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import edu.icesi.hobbies.databinding.ActivityMainBinding
import edu.icesi.hobbies.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextLoginName.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            Firebase.auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                val fbuser = Firebase.auth.currentUser

                if(fbuser!!.isEmailVerified){

                    Firebase.firestore.collection("users").document(fbuser.uid).get().addOnSuccessListener {
                        val user = it.toObject(User::class.java)

                        saveUser(user!!)
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    }
                }else{
                    Toast.makeText(this,"El Email no se encuentra verificado",Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener{
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

    }

    private fun saveUser(user: User) {
        val sp = getSharedPreferences("hobbies-app", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user",json).apply()
    }
}