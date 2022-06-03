package edu.icesi.hobbies.model

data class Chat(
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)