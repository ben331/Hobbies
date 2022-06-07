package edu.icesi.hobbies.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.icesi.hobbies.databinding.FragmentProfileBinding
import edu.icesi.hobbies.model.User

class ProfileFragment : Fragment() {
    private lateinit var user: User
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private var mStorageRef: StorageReference? = null
    private  var mImageUri: Uri? = null

    private  var photo:Int ?= null

    //Gallery Launchers
    private val launcherCover = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::onActivityResult)

    //Volley
    private lateinit var queue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        queue = Volley.newRequestQueue(activity)

        binding.profilePhoto.setOnClickListener{
            photo=1
            openFileChooser()
        }
        binding.coverPhoto.setOnClickListener{
            photo=2
            openFileChooser()
        }
        binding.logoutBtn.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        val userId = Firebase.auth.currentUser?.uid!!

        Log.e(">>>>>>>>>>>>", "on create")
        Firebase.firestore.collection("users").document(userId).get().addOnSuccessListener {
            Log.e(">>>>>>>>>>>>", "en firebase")
            user = it.toObject(User::class.java)!!
            loadImageFromWebOperationsProfile(user.profileURI)
            loadImageFromWebOperationsCover(user.coverURI)
        }.addOnFailureListener{
            Log.e("Death", "Death")
        }

        Log.e(">>>>>>>>>>>>", "despues de firebase")

        return view
    }
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        launcherCover.launch(intent)
    }

    private fun onActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            mImageUri = result.data?.data
            if(photo==1){
                user.profileURI=mImageUri.toString()
                binding.profilePhoto.setImageURI(mImageUri)
                uploadFile()
            }else{
                user.coverURI=mImageUri.toString()
                binding.coverPhoto.setImageURI(mImageUri)
                uploadFile()
            }

        }
    }
    override fun onResume() {
        super.onResume()

        Log.e(">>>>>>>>>>>>", "on start")

        val userId = Firebase.auth.currentUser?.uid!!
        Firebase.firestore.collection("users").document(userId).get().addOnSuccessListener {
            user = it.toObject(User::class.java)!!
            binding.dateView.text=user.birthday
            binding.emailView.text=user.email
            loadImageFromWebOperationsProfile(user.profileURI)
            loadImageFromWebOperationsCover(user.coverURI)
        }.addOnFailureListener{
            Log.e("Death", "Death")
        }
    }

    private fun uploadFile(){
        var filename = user.id
        filename = if(photo==1){
            "profile/${filename}"
        }else{
            "cover/${filename}"
        }

        mStorageRef= FirebaseStorage.getInstance().getReference("images/user/$filename")
        mImageUri?.let { it ->
            mStorageRef!!.putFile(it)
                .addOnSuccessListener{
                    Log.e("ALL RIGHT","")
                    mStorageRef!!.downloadUrl.addOnSuccessListener {result->
                        updateUser(result.toString(), photo!!)
                    }
                }
        }
    }

    private fun updateUser(url: String?, option:Int){
        val field = if(option==1){
            "profileURI"
        }else{
            "coverURI"
        }
        Firebase.firestore.collection("users").document(user.id).update(field,url).addOnSuccessListener {
            Toast.makeText(activity, "Profile updated", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(activity, "Failed to update profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadImageFromWebOperationsProfile(url:String?) {

        Log.e(">>>>>>>>>>>>", "antes de cargar perfil")

        if(url!=null && url!="null" && url!=""){
            val imgRequest = ImageRequest( url,{ bitmap->

                Log.e(">>>>>>>>>>>>", "perfil Cargado")
                binding.profilePhoto.setImageBitmap(bitmap)
            },0,0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Log.e(">>>>>>>>>>>>", "Failed to load image of club")
            })
            queue.add(imgRequest)
            Log.e(">>>>>>>>>>>>", "cargando perfil")
        }
    }

    private fun loadImageFromWebOperationsCover(url:String?) {

        Log.e(">>>>>>>>>>>>", "antes de cover")
        if(url!=null && url!="null" && url!=""){
            val imgRequest = ImageRequest( url,{ bitmap->
                Log.e(">>>>>>>>>>>>", "cover cargado")
                binding.coverPhoto.setImageBitmap(bitmap)
            },0,0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Log.e(">>>>>>>>>>>>", "Failed to load image of club")
            })
            queue.add(imgRequest)
            Log.e(">>>>>>>>>>>>", "cargando cover")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}