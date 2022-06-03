package edu.icesi.hobbies.model

class User(var id:String, var name:String, var phone:String, var email:String, var profileURI:String) {

    private lateinit var contacts:ArrayList<User>
    private lateinit var hobbies:ArrayList<Hobby>
    private lateinit var clubs:ArrayList<Club>
}