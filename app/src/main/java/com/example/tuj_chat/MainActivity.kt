package com.example.tuj_chat

import LoginScreen
import RegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.tuj_chat.ui.theme.MainScreen
import com.example.tuj_chat.ui.theme.TUJ_ChatTheme
import com.example.tuj_chat.ui.theme.screens.HomeScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tuj_chat.data.FirebaseManager
import com.example.tuj_chat.data.User
import com.example.tuj_chat.ui.theme.screens.CallScreen
import com.example.tuj_chat.ui.theme.screens.CameraScreen
import com.example.tuj_chat.ui.theme.screens.ChatScreen
import com.example.tuj_chat.ui.theme.screens.SearchScreen

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setContent {
            TUJ_ChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }

    @Composable
    fun AppContent() {
        val currentUser = auth.currentUser
        val (currentScreen, setCurrentScreen) = remember { mutableStateOf(Screen.Login) }

        when {

            currentScreen == Screen.ChatScreen -> {
                user?.let {
                    ChatScreen(it, it!!.id) {
                        setCurrentScreen(Screen.PersonalChatScreen)
                    }
                }
            }

            currentScreen == Screen.SearchScreen -> {
                SearchScreen(FirebaseManager()) {
                    user = it
                    setCurrentScreen(Screen.ChatScreen)
                }
            }

            currentScreen == Screen.PersonalChatScreen -> {
                HomeScreen(navigateToChatScreen = {
                    user = it.convertUser()
                    setCurrentScreen(Screen.ChatScreen)
                },
                    onClickSearch = {
                        setCurrentScreen(Screen.SearchScreen)
                    },
                    onClickBack = {
                        setCurrentScreen(Screen.Main)
                    })
            }

            currentUser != null -> {
                // User is authenticated, show MainScreen
                MainScreen(onLogout = { setCurrentScreen(Screen.Login) }) {
                    setCurrentScreen(Screen.PersonalChatScreen)
                }
            }

            currentScreen == Screen.Login -> {
                // Show login screen
                LoginScreen(
                    navigateToRegistration = { setCurrentScreen(Screen.Registration) },
                    onLoginSuccess = { setCurrentScreen(Screen.Main) }
                )
            }

            currentScreen == Screen.Registration -> {
                // Show registration screen
                RegistrationScreen(
                    navigateToLogin = { setCurrentScreen(Screen.Login) }
                )
            }

            else -> {
                // Show login screen by default
                LoginScreen(
                    navigateToRegistration = { setCurrentScreen(Screen.Registration) },
                    onLoginSuccess = { setCurrentScreen(Screen.Main) }
                )
            }
        }
    }

    enum class Screen {
        Login,
        Registration,
        Main,
        PersonalChatScreen,
        SearchScreen,
        ChatScreen
    }
}



