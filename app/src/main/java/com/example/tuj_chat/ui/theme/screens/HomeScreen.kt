package com.example.tuj_chat.ui.theme.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.blc.darkchat.data.ConversationMapper

import com.example.tuj_chat.data.FirebaseManager
import com.example.tuj_chat.data.User
import com.example.tuj_chat.ui.theme.components.ConversationItem
import com.example.tuj_chat.ui.theme.components.HomeAppBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    chatManager: FirebaseManager = FirebaseManager(),
    navigateToChatScreen: (ConversationMapper) -> Unit,
    onClickSearch: () -> Unit,
    onClickBack: () -> Unit) {

    var listOfConversations by remember { mutableStateOf(emptyList<ConversationMapper>()) }

    LaunchedEffect(chatManager) {
        chatManager.receiveConversations(FirebaseAuth.getInstance().uid!!) { conversations ->
            listOfConversations = conversations
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar(onClickSearch, onClickBack)
        },
        containerColor = Color(0xFF1C1B1B),
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            items(listOfConversations) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = {
                        navigateToChatScreen(conversation)
                    }
                )
            }
        }
    }
}
