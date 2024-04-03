import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import com.example.tuj_chat.data.FirebaseManager

@Composable
fun RegistrationScreen(navigateToLogin: () -> Unit) {
    // State to hold the email and password entered by the user
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    // State to track password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    // Firebase authentication instance
    val auth = FirebaseAuth.getInstance()

    // Context
    val context = LocalContext.current

    // Handle registration button click
    val registerButtonClick = registerButtonClick@{
        val email = emailState.value
        val password = passwordState.value

        // Check if the email matches the allowed domain
        if (!email.endsWith("@temple.edu")) {
            // Display an error message or toast indicating invalid email domain
            Toast.makeText(context, "Please use a @temple.edu email address", Toast.LENGTH_SHORT).show()
            return@registerButtonClick  // Exit the function if email domain is invalid
        }

        // Call Firebase authentication to create a new user account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()

                    val firebaseManager = FirebaseManager()
                    firebaseManager.addUser(email, "")
                    // Send email verification
                    sendEmailVerification(context, auth)

                    // Navigate to the login screen or another screen
                } else {
                    // Registration failed
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }

    // UI code starts here
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFFFFE0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center, // Align children vertically in the center
            horizontalAlignment = Alignment.CenterHorizontally // Align children horizontally in the center
        ) {
            // Title
            Text(
                text = "Create New Account", // Text to display
                style = MaterialTheme.typography.headlineSmall, // Typography style
                modifier = Modifier.padding(bottom = 16.dp) // Add bottom padding
            )

            // Email input field
            OutlinedTextField(
                value = emailState.value, // Current value of the email field
                onValueChange = { emailState.value = it }, // Update emailState when value changes
                label = { Text("Email") }, // Label for the email field
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next), // Specify keyboard action
                keyboardActions = KeyboardActions(onNext = { /* Handle next action */ }) // Specify keyboard action handler
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical spacing

            // Password input field with visibility toggle

            OutlinedTextField(
                value = passwordState.value, // Current value of the password field
                onValueChange = { passwordState.value = it }, // Update passwordState when value changes
                label = { Text("Password") }, // Label for the password field
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Show plain text or password
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done), // Specify keyboard action
                keyboardActions = KeyboardActions(onDone = { /* Handle done action */ }), // Specify keyboard action handler
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisible = !passwordVisible }
                    ) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Check else Icons.Filled.CheckCircle,
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp)) // Add vertical spacing

            // Registration button
            Button(
                onClick = { registerButtonClick() }, // Call registerButtonClick lambda function when button is clicked
                modifier = Modifier.fillMaxWidth() // Button fills the maximum available width
            ) {
                Text("Register") // Text displayed on the button
            }

            // Button to navigate back to login screen
            TextButton(
                onClick = navigateToLogin,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Already have an account? Log in")
            }
        }
    }
}

// Define the sendEmailVerification function outside of the RegistrationScreen composable
private fun sendEmailVerification(context: Context, auth: FirebaseAuth) {
    val user = auth.currentUser
    user?.sendEmailVerification()
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Email verification sent
                Toast.makeText(context, "Verification email sent", Toast.LENGTH_SHORT).show() } else {
                // Failed to send verification email
                Toast.makeText(context, "Failed to send verification email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}