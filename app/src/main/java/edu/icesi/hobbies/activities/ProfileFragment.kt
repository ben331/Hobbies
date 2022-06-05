package edu.icesi.hobbies.activities

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.icesi.hobbies.databinding.FragmentProfileBinding
import edu.icesi.hobbies.model.User
import java.io.InputStream
import java.net.URL
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var user: User
    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private var mStorageRef: StorageReference? = null
    private  var mImageUri: Uri? = null
    private  var photo:Int ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        val userId = Firebase.auth.currentUser?.uid!!
        Firebase.firestore.collection("users").document(userId).get().addOnSuccessListener {
            user = it.toObject(User::class.java)!!
        }.addOnFailureListener{
            Log.e("Death", "Death")
        }


        binding.profilePhoto.setOnClickListener{
            photo=1
            openFileChooser()
        }
        binding.coverPhoto.setOnClickListener{
            photo=2
            openFileChooser()
        }
        val thread = Thread {
            try {
                //var drawProfile = LoadImageFromWebOperationsProfle(user.profileURI)
                //var drawcoverProfile = LoadImageFromWebOperationsCover(user.coverURI)
                //binding.profilePhoto.setImageDrawable(drawProfile)
                //binding.coverPhoto.setImageDrawable(drawcoverProfile)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        return view
    }
    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            val bitmap=MediaStore.Images.Media.getBitmap(context?.contentResolver,mImageUri)
            val bitmapD= BitmapDrawable(bitmap)
            if(photo==1){
                user.profileURI=mImageUri.toString()
                binding.profilePhoto?.setBackgroundDrawable(bitmapD)
                uploadFile()
            }else{
                user.coverURI=mImageUri.toString()
                binding.coverPhoto?.setBackgroundDrawable(bitmapD)
                uploadFile()
            }

        }
    }
    override fun onStart() {
        super.onStart()
        binding.dateView.text=user.birthday
        binding.emailView.text=user.email
    }
    private fun uploadFile(){
        val filename= UUID.randomUUID().toString()
        mStorageRef= FirebaseStorage.getInstance().getReference("/$filename")
        mImageUri?.let {
            mStorageRef!!.putFile(it)
                .addOnSuccessListener{
                    Log.e("TODO BIEN","")
                    mStorageRef!!.downloadUrl.addOnSuccessListener {
                        it.toString()
                    }
                }
        }
    }
    fun LoadImageFromWebOperationsProfle(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "avatar")
        } catch (e: Exception) {
            Log.d("Exception: ",e.toString())
            null
        }
    }
    fun LoadImageFromWebOperationsCover(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).getContent() as InputStream
            Drawable.createFromStream(`is`, "avatar")
        } catch (e: Exception) {
            Log.d("Exception: ",e.toString())
            null
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}