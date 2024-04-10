package com.example.tuj_chat.data

import com.blc.darkchat.data.ConversationMapper
import com.blc.darkchat.data.MessageGroup
import com.blc.darkchat.data.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseManager {
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val messagesRef = database.getReference("messages")
    private val messagesGroupRef = database.getReference("messages_group")
    private val conversationsRef = database.getReference("conversations")
    private val clubsRef = database.getReference("clubs")

    fun searchUsers(keyword: String, onResult: (List<User>) -> Unit) {
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                snapshot.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user != null && user.email.contains(keyword, ignoreCase = true)
                        && user.email != FirebaseAuth.getInstance().currentUser?.email!!) {
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
    fun sendMessageGroup(message: MessageGroup) {
        val messageId = System.currentTimeMillis();
        messagesGroupRef.child("$messageId").setValue(message)
    }

    fun receivedMessages(onReceive: (List<MessageGroup>) -> Unit){
        messagesGroupRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    val listMessageGroup = mutableListOf<MessageGroup>()
                    snapshot.children.forEach {
                        val messageGroup = it.getValue(MessageGroup::class.java)
                        messageGroup?.let {mGroup ->
                            listMessageGroup.add(mGroup)
                        }
                    }
                    onReceive(listMessageGroup)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
                        println("senderId = ${message.senderId} - receiverId = ${message.receiverId} - $receiverId ")
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
    suspend fun fetchClubs(): List<Clubs> = suspendCoroutine { continuation ->
        clubsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val clubsList = mutableListOf<Clubs>()

                for (snapshot in dataSnapshot.children) {
                    val title = snapshot.child("title").getValue(String::class.java) ?: ""
                    val leader = snapshot.child("leader").getValue(String::class.java) ?: ""
                    val description = snapshot.child("description").getValue(String::class.java) ?: ""
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val club = Clubs(title, description, leader, imageUrl)
                    clubsList.add(club)
                }

                continuation.resume(clubsList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                continuation.resume(emptyList()) // Return empty list in case of error
            }
        })
    }
}

