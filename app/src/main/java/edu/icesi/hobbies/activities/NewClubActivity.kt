package edu.icesi.hobbies.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.icesi.hobbies.databinding.ActivityNewClubBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.Hobby
import edu.icesi.hobbies.model.User
import java.util.*

class NewClubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClubBinding

    private var mStorageRef: StorageReference? = null
    private var mImageUri: Uri? = null
    private var mImageUrl: Uri? = null
    private var clubId: String = UUID.randomUUID().toString()

    //Gallery Launchers
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onActivityResult)

    private lateinit var currentUser: User
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityNewClubBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val id = Firebase.auth.currentUser?.uid
        Firebase.firestore.collection("users").document(id!!).get().addOnSuccessListener {
            currentUser = it.toObject(User::class.java)!!
        }
        binding.btnAdd.setOnClickListener {
            requestStoragePermissions()
        }

        binding.btnClubCreate.setOnClickListener {
            val name = binding.editTextClubName.text.toString()
            val hobbyName = binding.editTextClubHobbie.text.toString()

            if(name.isEmpty() or hobbyName.isEmpty()){
                Toast.makeText(this,"Empty values", Toast.LENGTH_SHORT).show()
            }else{
                val hobby = Hobby(UUID.randomUUID().toString(),hobbyName, "")
                val adminId = currentUser.id

                Log.e(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Uri", mImageUrl.toString())
                //Create Club----------------------------------------------------------------------
                val club = Club(clubId,name,hobby,adminId, mImageUrl.toString())

                Firebase.firestore.collection("users").document(currentUser.id).collection("clubs").document(clubId).set(club).addOnSuccessListener {
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }

                Firebase.firestore.collection("clubs").document(clubId).set(club).addOnSuccessListener {
                    mImageUri = null
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this,it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun uploadFile(){
        mStorageRef= FirebaseStorage.getInstance().getReference("images/club/$clubId")
        mImageUri?.let { it ->
            mStorageRef!!.putFile(it)
                .addOnSuccessListener{
                    Log.e("ALL RIGHT","")
                    mStorageRef!!.downloadUrl.addOnSuccessListener {result->
                        mImageUrl = result
                    }
                }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcher.launch(intent)
    }

    private fun onActivityResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            mImageUri = result.data?.data
            binding.imgClub.setImageURI(mImageUri)
            uploadFile()
        }
    }

    //-----------------------------------------------   PERMISSIONS   ---------------------------------------------
    private fun requestStoragePermissions(){
        super.requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            ),
            3
        )
    }

    //Result after ask for permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var haveLocationPermissions = true
        for (result in grantResults) {
            haveLocationPermissions = haveLocationPermissions && (result!=-1)
        }
        if(haveLocationPermissions) openFileChooser()
    }
}