package edu.icesi.hobbies.model

import java.io.Serializable

data class Club(

    private val id:String = "",
    private var name:String = "",
    private var hobby:Hobby,
    private var users:ArrayList<User> = ArrayList(),
    private var admin:Admin ,
    private var chat:Chat = Chat(),
    private var events:ArrayList<Event> = ArrayList(),
    private var image:String="",
) :Serializable