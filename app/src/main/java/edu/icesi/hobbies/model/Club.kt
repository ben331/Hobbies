package edu.icesi.hobbies.model

import java.io.Serializable

data class Club(

    val id:String = "",
    var name:String = "",
    var hobby:Hobby,
    var users:ArrayList<User> = ArrayList(),
    var admin:Admin ,
    var chat:Chat = Chat(),
    var events:ArrayList<Event> = ArrayList(),
    var image:String="",
) :Serializable