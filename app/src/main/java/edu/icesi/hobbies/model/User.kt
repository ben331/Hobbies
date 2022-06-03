package edu.icesi.hobbies.model

import java.io.Serializable

open class User(): Serializable{
    var id:String = "Dummy"
    var name:String = "Dummy"
    var phone:String = "3158033739"
    var email:String = "Dummy"
    var birthday: String = "Dummy"
    var profileURI:String = ""

    lateinit var contacts:ArrayList<User>
    lateinit var hobbies:ArrayList<Hobby>
    lateinit var clubs:ArrayList<Club>

    constructor(id:String, name:String, birthday:String, email:String) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.birthday = birthday
        this.contacts = ArrayList()
        this.hobbies = ArrayList()
        this.clubs = ArrayList()
    }
}
