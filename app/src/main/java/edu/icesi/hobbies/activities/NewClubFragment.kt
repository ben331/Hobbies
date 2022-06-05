package edu.icesi.hobbies.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.icesi.hobbies.R



class NewClubFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_club, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = NewClubFragment()
    }
}