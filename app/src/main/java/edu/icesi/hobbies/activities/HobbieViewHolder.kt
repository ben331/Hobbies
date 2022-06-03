package edu.icesi.hobbies.activities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R

class HobbieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var hobbieName : TextView = itemView.findViewById(R.id.imageHobbie)
    var hobbieImage: ImageView =  itemView.findViewById(R.id.name_hobbie)

}