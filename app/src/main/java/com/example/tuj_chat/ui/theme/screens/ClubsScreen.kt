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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuj_chat.data.Clubs


@Composable
fun ClubsScreen() {
    // Fetch
    // :clubs from Firebase
    val clubsList = remember { mutableStateOf(emptyList<Clubs>()) }

    LaunchedEffect(key1 = true) {
        clubsList.value = FirebaseManager().fetchClubs()
    }

    Column {
        ClubsList(clubsList = clubsList.value)
    }
}

@Composable
fun ClubsList(clubsList: List<Clubs>) {
    LazyColumn {
        items(clubsList) { club ->
            ClubCard(club = club)
        }
    }
}

@Composable
fun ClubCard(club: Clubs) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = club.title,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = club.description,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = club.leader,
                    modifier = Modifier.padding(10.dp)
                )
            }

        }
    }
}

@Preview
@Composable
fun ClubCardPreview() {
    // Sample club for preview
    val club = Clubs("Sample Club", "Sample Leader", "sample_image_url")

    ClubCard(club = club)
}