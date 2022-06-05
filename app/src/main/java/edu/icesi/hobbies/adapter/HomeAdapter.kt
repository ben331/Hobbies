package edu.icesi.hobbies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R
import edu.icesi.hobbies.model.Club

class HomeAdapter: RecyclerView.Adapter<HomeViewHolder>() {

    private val clubs=ArrayList<Club>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.postrow, parent, false)
        return HomeViewHolder(row)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val clubN=clubs[position]
        holder.clubName.text=clubN.name
    }

    override fun getItemCount(): Int {
        return clubs.size
    }
    fun addClub(club: Club){
        this.clubs.add(club)
    }
}