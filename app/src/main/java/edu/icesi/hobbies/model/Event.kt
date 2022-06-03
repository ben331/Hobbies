package edu.icesi.hobbies.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class Event(
    val id:String="",
    val name:String="",
    val clubName:String="",
    val placeName:String="",
    val imgClubUri:String="",
    val chatClubId:String="",

    //Event GSC
    val lat:Double,
    val lng:Double

    ):Serializable