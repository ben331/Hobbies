package edu.icesi.hobbies.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.databinding.ActivitySignUpBinding
import edu.icesi.hobbies.model.User
import java.time.LocalDate

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

        binding.btnSignupConfirm.setOnClickListener{

            val name = binding.editTextSignupName.text.toString()
            val email = binding.editTextSignupEmail.text.toString()
            val date = binding.editTextDateSignupBirthday.text.toString()

            if(name.isEmpty() || email.isEmpty()||date.isEmpty()){
                Toast.makeText(this,"Empty fields",Toast.LENGTH_SHORT).show()
            }else{
                try {
                    val dates=date.split('/')
                    val day = dates[0].toInt()
                    val month = dates[1].toInt()
                    val year = dates[2].toInt()
                    val birthday = LocalDate.of(year,month,day)

                    if(birthday.isAfter(LocalDate.now().minusYears(18))){
                        Toast.makeText(this,"you must be of legal age",Toast.LENGTH_LONG).show()
                    }else{
                        register(name,email,date)
                    }
                }catch (e:Exception){
                    Toast.makeText(this,"Invalid date",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun register(name:String,email:String,birthday:String) {
        Firebase.auth.createUserWithEmailAndPassword(
            binding.editTextSignupEmail.text.toString(),
            binding.editTextSignupPassword1.text.toString()
        ).addOnSuccessListener {
            val id = Firebase.auth.currentUser?.uid
            val user = User(id!!,name,email,birthday)
            Firebase.firestore.collection("users").document(id).set(user).addOnSuccessListener {
                sendVerificationEmail()
                finish()
            }
        }.addOnFailureListener{
            Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
        }
    }

    private fun sendVerificationEmail(){
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this,"Check your email before to login",Toast.LENGTH_LONG).show()
        }?.addOnFailureListener{
            Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
        }
    }
}