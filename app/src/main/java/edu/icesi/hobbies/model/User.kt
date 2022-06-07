package edu.icesi.hobbies.model

import java.io.Serializable

open class User(): Serializable{
    var id:String = "Dummy"
    var name:String = "Dummy"
    //var phone:String = "3158033739"
    var email:String = "Dummy"
    var birthday: String = "Dummy"
    var profileURI:String = ""
    var coverURI:String = ""

    private lateinit var contactsId:ArrayList<String>
    lateinit var hobbies:ArrayList<Hobby>
    lateinit var requestBox:Chat

    constructor(id:String, name:String, email:String, birthday:String) : this() {
        this.id = id
        this.name = name
        this.email = email
        this.birthday = birthday
        this.contactsId = ArrayList()
        this.hobbies = ArrayList()
        requestBox = Chat()
    }
}
