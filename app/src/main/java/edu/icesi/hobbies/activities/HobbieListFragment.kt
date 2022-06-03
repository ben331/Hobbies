package edu.icesi.hobbies.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import edu.icesi.hobbies.R
import edu.icesi.hobbies.databinding.FragmentHobbieListBinding
import edu.icesi.hobbies.modules.Hobbie

class HobbieListFragment : Fragment() {
    private var _binding: FragmentHobbieListBinding?=null
    private val binding get() =_binding!!
    //STATE
    private var adapter = HobbieAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentHobbieListBinding.inflate(inflater,container, false)
        val view = binding.root

        //recrear el estado
        val postRecycler = binding.hobbiesRecycle
        postRecycler.setHasFixedSize(true)
        postRecycler.layoutManager = LinearLayoutManager(activity)

        postRecycler.adapter = adapter

        return view
    }
    companion object {

        @JvmStatic
        fun newInstance() = HobbieListFragment()
    }
    fun onNewHobbie(id:String, name:String, image: String) {
        val newHobbie = Hobbie(id,name,image)
        adapter.addHobbie(newHobbie)
    }

}