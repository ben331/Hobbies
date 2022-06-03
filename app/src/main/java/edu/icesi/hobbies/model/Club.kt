package edu.icesi.hobbies.model

class Club(id:String, name:String, hobby:Hobby, admin:Admin){

    private var id:String = id
    private var name:String = name
    private var hobby:Hobby = hobby
    private var users:ArrayList<User> = ArrayList()
    private var admin:Admin = admin
    private var chat:Chat = Chat()
    private var event:ArrayList<Events> = ArrayList()

}