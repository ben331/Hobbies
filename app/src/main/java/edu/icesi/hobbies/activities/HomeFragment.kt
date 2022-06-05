package edu.icesi.hobbies.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.adapter.HomeAdapter
import edu.icesi.hobbies.databinding.FragmentHomeBinding
import edu.icesi.hobbies.model.Club
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import edu.icesi.hobbies.adapters.ChatAdapter
import edu.icesi.hobbies.model.Chat
import edu.icesi.hobbies.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val view = binding.root
        //recrear el estado
        val clubRecycler = binding.clubRecycler
        clubRecycler.setHasFixedSize(true)
        clubRecycler.layoutManager = LinearLayoutManager(activity)

        adapter = HomeAdapter { club->
            clubSelected(club)
        }

        clubRecycler.adapter = adapter

        binding.clubRecycler.adapter = adapter

        binding.btnBuscarGrupos.setOnClickListener {
            val clubname = binding.searchClub.text.toString().lowercase()
            getClubsFromUserByClubname(clubname)
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
        getClubsfromUser()
        super.onStart()
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
}