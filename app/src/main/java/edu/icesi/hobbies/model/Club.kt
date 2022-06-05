package edu.icesi.hobbies.model

import java.io.Serializable

data class Club(
    val id:String = "",
    var name:String = "",
    var hobby:Hobby?=null,
    var adminId:String=""
) :Serializable