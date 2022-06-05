package edu.icesi.hobbies.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.time.LocalDate
import java.util.*

data class Event(
    val id:String="",
    val name:String="",
    val clubName:String="",
    val placeName:String="",
    val imgClubUri:String="",
    val chatClubId:String="",

    //Event GSC
    val pos:LatLng? = null,

    //
    val description:String="",
    val date:LocalDate?=null,
    val participants:Int=0
    ):Serializable