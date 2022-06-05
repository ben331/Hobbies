package edu.icesi.hobbies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R
import edu.icesi.hobbies.model.Club

class HomeAdapter(val chatClick:(Club) -> Unit): RecyclerView.Adapter<HomeViewHolder>() {

    private var clubs=ArrayList<Club>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.postrow, parent, false)
        return HomeViewHolder(row)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val clubN=clubs[position]
        holder.clubName.text=clubN.name

        holder.itemView.setOnClickListener{
            chatClick(clubs[position])
        }
    }

    override fun getItemCount(): Int {
        return clubs.size
    }
    fun addClub(club: Club){
        this.clubs.add(club)
        notifyItemInserted(clubs.size -1)
    }

    fun refreshClubs(clubs:ArrayList<Club>){
        this.clubs = clubs
        notifyDataSetChanged()
    }
}