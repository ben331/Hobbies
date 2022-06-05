package edu.icesi.hobbies.model

import java.io.Serializable

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
):Serializable