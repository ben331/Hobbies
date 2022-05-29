package edu.icesi.hobbies.modules

import java.io.Serializable
import java.time.LocalDate
import java.util.*

class User(): Serializable{
    var id:String = "Dummy"
    var name:String = "Dummy"
    var email:String = "Dummy"
    var birthday: String = "Dummy"

    constructor( id:String, name:String, email:String, birthday: String) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.birthday = birthday
    }
}