package edu.icesi.hobbies.model

import java.io.Serializable

class Admin(id:String="", name:String="", birthday:String="", email:String="", clubId:String=""):User(id, name, birthday, email), Serializable {
}