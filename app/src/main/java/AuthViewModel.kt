import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // State flow to represent the authentication state
    private val _isLoggedIn = MutableStateFlow(auth.currentUser != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // Function to handle user login
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                } else {
                    // Handle login failure
                }
            }
    }

    // Function to handle user registration
    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true
                } else {
                    // Handle registration failure
                }
            }
    }

    // Function to handle user logout
    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
    }
}
