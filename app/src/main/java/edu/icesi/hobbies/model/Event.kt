package edu.icesi.hobbies.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.time.LocalDate

data class Event(
    val id:String="",
    val name:String="",
    val clubName:String="",
    val placeName:String="",
    val imgClubUri:String="",
    val chatClubId:String="",

    //Event GSC
    val lat:Double=0.0,
    val lon:Double=0.0,

    //
    val description:String="",
    val day:Int=0,
    val month:Int= 0,
    val year:Int = 0,
    val participants:Int=0
    ):Serializable