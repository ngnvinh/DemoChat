package com.blc.darkchat.data

import com.example.tuj_chat.R
import com.example.tuj_chat.data.User
import com.google.firebase.database.Exclude


data class Conversation(
    val sender: String,
    val image: Int,
    val amILastSender: Boolean,
    val message: String,
    val time: String,
    val unread: Boolean,
)


class ConversationMapper(user: User = User(),  message: Messages = Messages()){
    val sender: String
    val image: String = ""
    val amILastSender: Boolean = false
    val message: String
    val time: Long
    val unread: Boolean
    val email: String

    init {
        sender = message.receiverId
        this.message = message.content
        time = message.timestamp
        unread = false
        email = user.email
    }

    fun convertUser(): User = User(id = sender, email = email, avatar = "")

}
