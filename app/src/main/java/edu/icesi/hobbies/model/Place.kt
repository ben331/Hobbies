package edu.icesi.hobbies.model

class Place(id:String, name:String, type:Char) {

    private var id = id
    private var name = name
    private var type = type
    private var historyClubs:ArrayList<Club> = ArrayList()
    private var events:ArrayList<Club> = ArrayList()

}