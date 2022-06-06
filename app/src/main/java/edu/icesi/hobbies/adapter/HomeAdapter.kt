package edu.icesi.hobbies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import edu.icesi.hobbies.R
import edu.icesi.hobbies.model.Club


class HomeAdapter(val chatClick:(Club) -> Unit): RecyclerView.Adapter<HomeViewHolder>() {

    private var clubs=ArrayList<Club>()
    
    //Listener to download images at home-fragment
    lateinit var listener: OnLoadImageListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val row = inflater.inflate(R.layout.postrow, parent, false)
        return HomeViewHolder(row)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val clubN=clubs[position]
        holder.clubName.text=clubN.name

        listener.downloadImage(clubN.imageUri, holder.clubImage)

        holder.itemView.setOnClickListener{
            chatClick(clubs[position])
        }
    }



    override fun getItemCount(): Int {
        return clubs.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshClubs(clubs:ArrayList<Club>){
        this.clubs = clubs
        notifyDataSetChanged()
    }

    interface OnLoadImageListener {
        fun downloadImage(url:String?, img:ImageView)
    }
}