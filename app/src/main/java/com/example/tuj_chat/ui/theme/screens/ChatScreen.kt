package com.example.tuj_chat.ui.theme.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.blc.darkchat.data.Messages
import com.example.tuj_chat.data.FirebaseManager
import com.example.tuj_chat.data.User
import com.example.tuj_chat.ui.theme.components.ChatAppBar
import com.example.tuj_chat.ui.theme.components.MessageBox
import com.example.tuj_chat.ui.theme.components.MessageInputField
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(user: User?, receiverId: String, onClickBack: () -> Unit) {
    val chatManager = FirebaseManager()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val (messages, setMessages) = remember { mutableStateOf<List<Messages>>(emptyList()) }

    LaunchedEffect(receiverId) {
        chatManager.receiveMessages(receiverId) { receivedMessages ->
            setMessages(receivedMessages.reversed())
        }
    }

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            ChatAppBar(user!!){
                onClickBack()
            }
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                reverseLayout = true,
            ) {
                items(messages) { message ->
                    MessageBox(message = message)
                }
            }

            MessageInputField(onMessageSent = { content ->
                val message = Messages(
                    senderId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    receiverId = receiverId,
                    content = content
                )
                chatManager.sendMessage(message, user)
            })
        }
    }
}
