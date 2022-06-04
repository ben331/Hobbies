package edu.icesi.hobbies.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import edu.icesi.hobbies.R
import edu.icesi.hobbies.adapter.HomeAdapter
import edu.icesi.hobbies.databinding.FragmentHomeBinding
import edu.icesi.hobbies.model.Club


class HomeFragment : Fragment() {
    //STATE
    private var adapter = HomeAdapter()
    private var _binding: FragmentHomeBinding?=null
    private val binding get() =_binding!!
    private var username:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        val view = binding.root
        username= intent.extras?.getString("username")
        //recrear el estado
        val clubRecycler = binding.clubRecycler
        clubRecycler.setHasFixedSize(true)
        clubRecycler.layoutManager = LinearLayoutManager(activity)

        clubRecycler.adapter = adapter
        binding.btnBuscarGrupos.setOnClickListener {
            val clubname = binding.searchClub.text.toString().lowercase()
            getClubsFromUserByClubname(clubname)
        }
        return view
    }
    private fun getClubsFromUserByClubname(clubName: String){
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                adapter.clean()
            }
        }
        val query = Firebase.firestore.collection("clubs").whereEqualTo("username", username)
        query.get().addOnCompleteListener { task ->
            if(task.result?.size() != 0){
                for(document in task.result!!){
                    val club = document.toObject(Club::class.java)
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            if(club.name.startsWith(clubName.lowercase())){
                                adapter.addClub(club)
                            }
                        }
                    }
                }
            }else{
                Toast.makeText(getApplicationContext(); ,"Actualmente no tienes clubes", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun getClubsfromUser(){
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                adapter.clean()
            }
        }
        val query = Firebase.firestore.collection("clubs").whereEqualTo("user", username)
        query.get().addOnCompleteListener { task ->
            if(task.result?.size() != 0){
                for(document in task.result!!){
                    val club = document.toObject(Club::class.java)
                    lifecycleScope.launch(Dispatchers.IO){
                        withContext(Dispatchers.Main){
                            adapter.addClub(club)
                        }
                    }
                }
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
}