
package com.example.tuj_chat.ui.theme


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.tuj_chat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun MainScreen(onLogout: () -> Unit,
               onClickChatForum: () -> Unit,
               onClickPersonalScreen: () -> Unit
) {
    var isTUPortalOpened by remember { mutableStateOf(false) }
    var loggedInUser by remember { mutableStateOf<FirebaseUser?>(null) }

    // Check if the user is logged in when the MainScreen is first composed
    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        loggedInUser = auth.currentUser
    }

    // Fetch the user's email if logged in
    val email = loggedInUser?.email ?: "Guest"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display user login status
            Text(
                text = "Logged in as: $email",
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle hamburger button click */ },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }

                UserAvatarDropdown(
                    onLogoutClicked = onLogout,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Temple University",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillWidth
            )

            SectionButton(text = "School Announcement", onClick = {}, backgroundColor = Color(0xA1001747))
            SectionButton(text = "Forum", onClick = {
                println("Forum")
                onClickChatForum()
            }, backgroundColor = Color(0xA1001747))
            SectionButton(
                text = "Personal Chat",
                onClick = {
                    onClickPersonalScreen()
                },
                backgroundColor = Color(0xA1001747)
            )
            SectionButton(
                text = "TU Portal",
                onClick = { isTUPortalOpened = true },
                backgroundColor = Color(0xA1001747)
            )

            // TU Portal WebView
            TUPortalWebView(isOpen = isTUPortalOpened, onClose = { isTUPortalOpened = false })
        }
    }
}



@Composable
fun TUPortalWebView(
    isOpen: Boolean,
    onClose: () -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onClose,
            content = {
                Box(Modifier.fillMaxSize()) {
                    TUPortalWebViewContent()
                }
            }
        )
    }
}



@Composable
private fun TUPortalWebViewContent() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://tuportal6.temple.edu/group/home/student-tools")
            }
        }
    )
}

// UserAvatarDropdown composable function

@Composable
fun UserAvatarDropdown(onLogoutClicked: () -> Unit, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = { expanded = !expanded })
        ) {
            // Placeholder for user avatar image
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            DropdownMenuItem(text = { Text("Settings") }, onClick = {
                expanded = false
                // Handle settings click
            })
            DropdownMenuItem(text = { Text("Log out") }, onClick = {
                expanded = false
                auth.signOut()
                onLogoutClicked()
            })
        }
    }
}

@Composable
fun SectionButton(text: String, onClick: () -> Unit, backgroundColor: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text)
    }
}





/*

package com.example.tuj_chat.ui.theme


import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.tuj_chat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun MainScreen(onLogout: () -> Unit, onNavigateToForum: () -> Unit) {
    var isTUPortalOpened by remember { mutableStateOf(false) }
    var loggedInUser by remember { mutableStateOf<FirebaseUser?>(null) }

    // Check if the user is logged in when the MainScreen is first composed
    LaunchedEffect(Unit) {
        val auth = FirebaseAuth.getInstance()
        loggedInUser = auth.currentUser
    }

    // Fetch the user's email if logged in
    val email = loggedInUser?.email ?: "Guest"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display user login status
            Text(
                text = "Logged in as: $email",
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { /* Handle hamburger button click */ },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu")
                }

                UserAvatarDropdown(
                    onLogoutClicked = onLogout,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Temple University",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.FillWidth
            )

            SectionButton(text = "School Announcement", onClick = {}, backgroundColor = Color(0xA1001747))
            SectionButton(text = "Forum", onClick = onNavigateToForum, backgroundColor = Color(0xA1001747))
            SectionButton(
                text = "Personal Chat",
                onClick = {},
                backgroundColor = Color(0xA1001747)
            )
            SectionButton(
                text = "TU Portal",
                onClick = { isTUPortalOpened = true },
                backgroundColor = Color(0xA1001747)
            )

            // TU Portal WebView
            TUPortalWebView(isOpen = isTUPortalOpened, onClose = { isTUPortalOpened = false })
        }
    }
}


@Composable
fun TUPortalWebView(
    isOpen: Boolean,
    onClose: () -> Unit
) {
    if (isOpen) {
        Dialog(
            onDismissRequest = onClose,
            content = {
                Box(Modifier.fillMaxSize()) {
                    TUPortalWebViewContent()
                }
            }
        )
    }
}



@Composable
private fun TUPortalWebViewContent() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl("https://tuportal6.temple.edu/group/home/student-tools")
            }
        }
    )
}

// UserAvatarDropdown composable function

@Composable
fun UserAvatarDropdown(onLogoutClicked: () -> Unit, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(onClick = { expanded = !expanded })
        ) {
            // Placeholder for user avatar image
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            DropdownMenuItem(text = { Text("Settings") }, onClick = {
                expanded = false
                // Handle settings click
            })
            DropdownMenuItem(text = { Text("Log out") }, onClick = {
                expanded = false
                auth.signOut()
                onLogoutClicked()
            })
        }
    }
}

@Composable
fun SectionButton(text: String, onClick: () -> Unit, backgroundColor: Color) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor),
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text)
    }
}







*/
