package edu.icesi.hobbies.modules

import java.io.Serializable
import java.time.LocalDate
import java.util.*

class User(): Serializable{
    var id:String = "Dummy"
    var name:String = "Dummy"
    var email:String = "Dummy"
    var birthday: String = "Dummy"
    var profileImage:String=""
    var coverImage:String=""

    constructor( id:String, name:String, email:String, birthday: String,profileImage: String,coverImage: String) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.birthday = birthday
        this.profileImage=profileImage
        this.coverImage=coverImage
    }
}