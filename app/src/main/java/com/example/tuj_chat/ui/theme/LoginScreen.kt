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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun LoginScreen(navigateToRegistration: () -> Unit, onLoginSuccess: () -> Unit) {
    // State to hold the email and password entered by the user
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // State to track password visibility
    var passwordVisible by remember { mutableStateOf(false) }

    // Firebase authentication instance
    val auth = FirebaseAuth.getInstance()

    // Context
    val context = LocalContext.current

    // Handle login button click
    val loginButtonClick = {
        // Call Firebase authentication to sign in the user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccess() // Invoke the onLoginSuccess callback
                } else {
                    // Login failed
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // UI code starts here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center, // Align children vertically in the center
        horizontalAlignment = Alignment.CenterHorizontally // Align children horizontally in the center
    ) {
        // Title
        Text(
            text = "Login to Your Account", // Text to display
            style = MaterialTheme.typography.headlineSmall, // Typography style
            modifier = Modifier.padding(bottom = 16.dp) // Add bottom padding
        )

        // Email input field
        OutlinedTextField(
            value = email, // Current value of the email field
            onValueChange = { email = it }, // Update email when value changes
            label = { Text("Email") }, // Label for the email field
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next), // Specify keyboard action
            keyboardActions = KeyboardActions(onNext = { /* Handle next action */ }) // Specify keyboard action handler
        )

        Spacer(modifier = Modifier.height(8.dp)) // Add vertical spacing

        // Password input field with visibility toggle
        OutlinedTextField(
            value = password, // Current value of the password field
            onValueChange = { password = it }, // Update password when value changes
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

        // Login button
        Button(
            onClick = { loginButtonClick() }, // Call loginButtonClick lambda function when button is clicked
            modifier = Modifier.fillMaxWidth() // Button fills the maximum available width
        ) {
            Text("Login") // Text displayed on the button
        }

        TextButton(
            onClick = navigateToRegistration,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Don't have an account? Register here")
        }
    }
}
