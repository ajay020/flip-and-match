package com.example.flipmatch.ui.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.flipmatch.R
import com.example.flipmatch.ui.components.StaticGameGrid
import com.example.flipmatch.utils.Routes

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    MainLoginContent(
        onSignInClick = {
            viewModel.signInWithGoogle(
                context,
                onSuccess = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() },
            )
        },
    )
}

@Composable
fun MainLoginContent(
    modifier: Modifier = Modifier,
    onSignInClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                        )
                    )
                )
                .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            StaticGameGrid()

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Welcome to Flip & Match!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            GoogleSignInButton {
               onSignInClick()
            }
        }
    }
}

@Composable
fun GoogleSignInButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(),
        modifier =
            Modifier
                .padding(8.dp)
                .height(50.dp)
                .fillMaxWidth(0.8f),
        elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.google_icon),
            contentDescription = "Google Sign-In",
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Sign in with Google",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MainLoginContentPreview() {
    MainLoginContent {  }
}
