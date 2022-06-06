package edu.icesi.hobbies.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.adapter.HomeAdapter
import edu.icesi.hobbies.databinding.FragmentHomeBinding
import edu.icesi.hobbies.model.Club
import edu.icesi.hobbies.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment(), HomeAdapter.OnLoadImageListener {

    //Firebase
    private var db = Firebase.firestore

    //Binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //Vars
    private var userId = Firebase.auth.currentUser?.uid!!
    private lateinit var adapter:HomeAdapter
    private lateinit var currentUser:User
    private lateinit var username:String

    //RequestQueue of Volley
    private lateinit var queue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val view = binding.root

        //Initialize Volley Queue
        queue = Volley.newRequestQueue(activity)

        //recrear el estado
        val clubRecycler = binding.clubRecycler
        clubRecycler.setHasFixedSize(true)
        clubRecycler.layoutManager = LinearLayoutManager(activity)

        adapter = HomeAdapter { club->
            clubSelected(club)
        }

        //Subscribe to adapter listener to load images
        adapter.listener = this

        clubRecycler.adapter = adapter

        binding.clubRecycler.adapter = adapter

        //Listener on Editext
        binding.searchClub.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val clubname = binding.searchClub.text.toString().lowercase()
                getClubsFromUserByClubname(clubname)
            }
        })

        binding.btnBuscarGrupos.setOnClickListener {
            binding.searchClub.visibility = View.VISIBLE
            binding.btnBuscarGrupos.visibility = View.GONE
            binding.btnCloseSearch.visibility = View.VISIBLE
        }

        binding.btnCloseSearch.setOnClickListener {
            binding.searchClub.setText("")

            binding.searchClub.let { v ->
                val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }

            binding.searchClub.visibility = View.GONE
            binding.btnBuscarGrupos.visibility = View.VISIBLE
            binding.btnCloseSearch.visibility = View.GONE
        }

        binding.btnNewClub.setOnClickListener{
            val intent = Intent(activity, NewClubActivity::class.java)
            startActivity(intent)
        }

        Firebase.firestore.collection("users").document(userId).get().addOnSuccessListener {
            currentUser = it.toObject(User::class.java)!!
            username = currentUser.name
        }.addOnFailureListener{
            Toast.makeText(activity, "Fail to load home", Toast.LENGTH_SHORT).show()
        }

        getClubsfromUser()

        return view
    }
    private fun getClubsFromUserByClubname(clubName: String){
        lifecycleScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main){
                //adapter.clean()
            }
        }
        val query = Firebase.firestore.collection("users").document(userId).collection("clubs")
        query.get().addOnCompleteListener { task ->

            val clubs:ArrayList<Club> = ArrayList()
            if(task.result?.size() != 0){
                for(document in task.result!!){
                    val club = document.toObject(Club::class.java)
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            if(club.name.startsWith(clubName.lowercase())){
                                clubs.add(club)
                            }
                        }
                    }
                }
                adapter.refreshClubs(clubs)

            }else{
                Toast.makeText(activity ,"Actualmente no tienes clubes", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getClubsfromUser(){
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                //adapter.clean()
            }
        }
        val query = Firebase.firestore.collection("users").document(userId).collection("clubs")
        query.get().addOnCompleteListener { task ->
            if(task.result?.size() != 0){
                val clubs:ArrayList<Club> = ArrayList()
                for(document in task.result!!){
                    val club = document.toObject(Club::class.java)
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            clubs.add(club)
                        }
                    }
                }
                adapter.refreshClubs(clubs)
                binding.welcomeText.visibility = View.GONE
            }else{
                binding.welcomeText.visibility = View.VISIBLE
            }
        }
    }
    override fun onStart() {
        super.onStart()
        getClubsfromUser()
    }
    override fun onResume(){
        super.onResume()
        getClubsfromUser()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private fun clubSelected(club: Club){
        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId", club.id)
        intent.putExtra("user", userId)
        startActivity(intent)
    }

    private fun newClub(club:Club){
        val otherChat = binding.searchClub.text.toString()
        val users = listOf(userId,otherChat)

        db.collection("clubs").document(club.id).set(club)
        db.collection("users").document(userId).collection("clubs").document(club.id).set(club)

        val intent = Intent(activity, ChatActivity::class.java)
        intent.putExtra("chatId", club.id)
        startActivity(intent)
    }

    override fun downloadImage(url: String?, img: ImageView) {
        if(url!=null && url!="null" && url!=""){
            Log.e("Error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", url)

            val imgRequest = ImageRequest( url,{ bitmap->
                img.setImageBitmap(bitmap)
            },0,0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Log.e(">>>>>>>>>>>>", "Faile to load image of club")
            })
            queue.add(imgRequest)
        }
    }
}