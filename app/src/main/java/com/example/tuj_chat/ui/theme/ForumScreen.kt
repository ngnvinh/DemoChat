package com.example.tuj_chat.ui.theme
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class Message(
    val userName: String,
    val text: String,
    val timestamp: Long
)

@Composable
fun ForumScreen() {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }

    // Function to send a message
    fun sendMessage() {
        // Ensure message text is not empty
        if (messageText.isNotEmpty()) {
            sendMessageToFirebase(messageText)
            messageText = ""
        }
    }

    // Listen for new messages
    LaunchedEffect(Unit) {
        // Fetch initial set of messages
        fetchInitialMessages { initialMessages ->
            messages.addAll(initialMessages)
        }

        // Listen for real-time updates
        listenForMessages { newMessages ->
            messages.addAll(newMessages)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Display messages
        LazyColumn {
            items(items = messages) { message ->
                Text(text = "${message.userName}: ${message.text}")
            }
        }

        // Input field to send messages
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter your message") }
            )

            Button(onClick = { sendMessage() }) {
                Text("Send")
            }
        }
    }
}

// Function to fetch initial set of messages
private fun fetchInitialMessages(callback: (List<Message>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("messages")
    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
            callback(messages)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}



// Sending Messages
private fun sendMessageToFirebase(message: String) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName = currentUser?.email ?: "Unknown User"
    val databaseReference = FirebaseDatabase.getInstance().getReference("messages")
    val messageData = Message(userName, message, System.currentTimeMillis())
    databaseReference.push().setValue(messageData)
}

// Receiving Messages
private fun listenForMessages(callback: (List<Message>) -> Unit) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("messages")
    val messageListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
            callback(messages)
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    }
    databaseReference.addValueEventListener(messageListener)
}
