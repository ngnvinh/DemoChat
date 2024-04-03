package com.example.tuj_chat.data

import com.blc.darkchat.data.ConversationMapper
import com.blc.darkchat.data.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FirebaseManager {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val messagesRef = database.getReference("messages")
    private val conversationsRef = database.getReference("conversations")

    fun searchUsers(keyword: String, onResult: (List<User>) -> Unit) {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                snapshot.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null && user.email.contains(keyword, ignoreCase = true) && user.email != FirebaseAuth.getInstance().currentUser?.email!!) {
                        users.add(user)
                    }
                }
                onResult(users)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun addUser(email: String, avatar: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val user = User(userId, email, avatar)
        usersRef.child(userId).setValue(user)
    }

    fun sendMessage(message: Messages, user: User?) {
        val messageId = messagesRef.push().key ?: ""
        messagesRef.child(messageId).setValue(message)

        conversationsRef.child(message.senderId).child(message.receiverId).setValue(
            ConversationMapper(user!!, message)
        )

        conversationsRef.child(message.receiverId).child(message.senderId).setValue(
            ConversationMapper(user!!.copy(email = FirebaseAuth.getInstance().currentUser?.email!!,
                id = message.senderId), message.copy(receiverId = message.senderId))
        )
    }

    fun receiveConversations( id: String,onReceive: (List<ConversationMapper>) -> Unit) {
        val conversationsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val conversations = mutableListOf<ConversationMapper>()
                snapshot.children.forEach { dataSnapshot ->
                    val conversation = dataSnapshot.getValue(ConversationMapper::class.java)
                    conversation?.let {
                        conversations.add(it)
                    }
                }
                onReceive(conversations)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        }
        conversationsRef.child(id).addValueEventListener(conversationsListener)


    }

    fun receiveMessages(receiverId: String, onReceive: (List<Messages>) -> Unit) {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Messages>()
                snapshot.children.forEach { dataSnapshot ->
                    val message = dataSnapshot.getValue(Messages::class.java)
                    if (message != null && (message.senderId == receiverId || message.receiverId == receiverId)) {
                        messages.add(message)
                    }
                }
                onReceive(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
