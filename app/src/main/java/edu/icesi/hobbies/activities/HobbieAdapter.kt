package edu.icesi.hobbies.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R
import edu.icesi.hobbies.modules.Hobbie

class HobbieAdapter: RecyclerView.Adapter<HobbieViewHolder>() {

    private val hobbies=ArrayList<Hobbie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbieViewHolder {
        var inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.postrow,parent,false)
        val hobbieViewHolder=HobbieViewHolder(row)
        return hobbieViewHolder
    }

    override fun onBindViewHolder(holder: HobbieViewHolder, position: Int) {
        val hobbieN=hobbies[position]
        holder.hobbieName.text=hobbieN.name
    }

    override fun getItemCount(): Int {
        return hobbies.size
    }
    fun addHobbie(hobbie: Hobbie){
        this.hobbies.add(hobbie)
    }
}