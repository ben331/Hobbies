package edu.icesi.hobbies.model

import java.io.Serializable
import java.util.*

data class Message (
    var message: String = "",
    var from: String = "",
    var dob: Date = Date()
): Serializable