package edu.icesi.hobbies.model

import java.io.Serializable

class Place(id:String="", name:String="", type:Int=0): Serializable {

    private var id = id
    private var name = name
    private var type = type
    private var historyClubs:ArrayList<Club> = ArrayList()
    private var events:ArrayList<Club> = ArrayList()
}