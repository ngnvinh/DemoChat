package com.blc.darkchat.data

data class Message(
    val message: String,
    val time: String,
    val isMe: Boolean
)

data class Messages(
    val senderId: String = "",
    val receiverId: String ="",
    val content: String ="",
    val timestamp: Long = System.currentTimeMillis()
)

