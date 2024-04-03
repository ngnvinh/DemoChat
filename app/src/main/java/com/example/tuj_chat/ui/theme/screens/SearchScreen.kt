package com.example.tuj_chat.ui.theme.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.example.tuj_chat.data.FirebaseManager
import com.example.tuj_chat.data.User

@Composable
fun SearchScreen(firebaseManager: FirebaseManager, onItemClick: (User) -> Unit) {
    var keyword by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<List<User>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = keyword,
            onValueChange = { keyword = it },
            placeholder = { Text("Enter keyword to search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                firebaseManager.searchUsers(keyword) { result ->
                    searchResult = result
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(searchResult) { user ->
                UserItem(user = user){
                    onItemClick(user)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick() } // Bắt sự kiện click ở đây
    ) {
        Text(
            text = user.email,
            modifier = Modifier.padding(16.dp)
        )
    }
}

