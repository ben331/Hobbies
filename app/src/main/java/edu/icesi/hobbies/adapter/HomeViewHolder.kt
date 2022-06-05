package edu.icesi.hobbies.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R

class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var clubImage : ImageView = itemView.findViewById(R.id.imageHobbie)
    var clubName: TextView =  itemView.findViewById(R.id.name_hobbie)

}